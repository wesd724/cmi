package com.jkb.cmi.orderbook.service;

import com.jkb.cmi.orderbook.dto.OrderBookDto;
import com.jkb.cmi.orderbook.dto.request.OrderRequest;
import com.jkb.cmi.orderbook.dto.response.ActiveOrderResponse;
import com.jkb.cmi.orderbook.dto.response.OrderBookResponse;
import com.jkb.cmi.currency.entity.Currency;
import com.jkb.cmi.orderbook.entity.OrderBook;
import com.jkb.cmi.sse.service.SseService;
import com.jkb.cmi.tradehistory.service.TradeHistoryService;
import com.jkb.cmi.user.entity.User;
import com.jkb.cmi.common.enums.Orders;
import com.jkb.cmi.tradehistory.entity.type.Status;
import com.jkb.cmi.currency.repository.CurrencyRepository;
import com.jkb.cmi.orderbook.repository.OrderBookRepository;
import com.jkb.cmi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderBookService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final OrderBookRepository orderBookRepository;
    private final TradeHistoryService tradeHistoryService;
    private final SseService sseService;

    public List<OrderBookResponse> getOrderBook(Long currencyId) {
        List<OrderBookDto> OrderBookDtoList = orderBookRepository.getByCurrencyId(currencyId);

        List<OrderBookDto> buyOrders = orderFilter(OrderBookDtoList, Orders.BUY);
        List<OrderBookDto> sellOrders = orderFilter(OrderBookDtoList, Orders.SELL);
        sellOrders.addAll(buyOrders);
        return OrderBookResponse.tolist(sellOrders);
    }

    public void orderProcessing(Long CurrencyId, Double price, Orders orders, OrderBook newOrder) {
        List<OrderBook> orderBookList = orderBookRepository.findByPriceForUpdate(
                CurrencyId, price, orders);

        orderBookRepository.save(newOrder);

        List<OrderBook> orderList = orderProcessFilter(orderBookList, orders);

        matchOrder(newOrder, orderList, orders);
    }

    public List<ActiveOrderResponse> getActiveOrder(String username) {
        List<OrderBook> orderBookList = orderBookRepository.getActiveOrderByUsername(username)
                .stream().sorted(
                        Comparator.comparingLong(OrderBook::getId)
                ).toList();
        return ActiveOrderResponse.tolist(orderBookList);
    }

    public void addOrder(OrderRequest orderRequest, Orders orders) {
        OrderBook newOrderBook = makeOrderBook(orderRequest, orders);

        orderProcessing(orderRequest.getCurrencyId(), orderRequest.getPrice(), orders, newOrderBook);

        sendOrderBook(orderRequest.getCurrencyId());
    }

    public OrderBook makeOrderBook(OrderRequest orderRequest, Orders orders) {
        User user = userRepository.getByUsername(orderRequest.getUsername());
        Currency currency = currencyRepository.getReferenceById(orderRequest.getCurrencyId());

        return orderRequest.toEntity(user, currency, orders);
    }

    public void sendOrderBook(Long currencyId) {
        List<OrderBookResponse> newOrderList = getOrderBook(currencyId);
        sseService.sendEventToAll("orderBook " + currencyId, newOrderList);
    }

    public void addVirtualOrder(Long userId, Long currencyId, Double amount, Double price, Orders orders) {
        User user = userRepository.getReferenceById(userId);
        Currency currency = currencyRepository.getReferenceById(currencyId);
        OrderBook newOrderBook = OrderBook.builder()
                .user(user).currency(currency).orders(orders)
                .originalAmount(amount).price(price).build();

        orderProcessing(currencyId, price, orders, newOrderBook);
    }

    public void matchOrder(OrderBook newOrder, List<OrderBook> orderList, Orders orders) {
        while (!orderList.isEmpty()) {

            OrderBook activeOrder = orderList.get(0);

            double tradeAmount = Math.min(newOrder.getActiveAmount(), activeOrder.getActiveAmount());
            //double tradePrice = Math.min(newOrder.getPrice(), activeOrder.getPrice());

            double newOrderAmount = newOrder.getActiveAmount() - tradeAmount;
            double activeOrderAmount = activeOrder.getActiveAmount() - tradeAmount;

            if (newOrder.getUser().getId() != 2L) {
                tradeHistoryService.saveTradeHistory(newOrder, activeOrder.getPrice(), tradeAmount,
                        newOrderAmount == 0 ? Status.COMPLETE : Status.PARTIAL);
            }

            if (activeOrder.getUser().getId() != 2L) {
                tradeHistoryService.saveTradeHistory(activeOrder, activeOrder.getPrice(), tradeAmount,
                        activeOrderAmount == 0 ? Status.COMPLETE : Status.PARTIAL);
            }


            if (activeOrderAmount == 0) {
                orderBookRepository.delete(activeOrder);
                orderList.remove(activeOrder);
            } else {
                activeOrder.changeActiveAmount(tradeAmount);
            }

            if (newOrderAmount == 0) {
                orderBookRepository.delete(newOrder);
                break;
            } else {
                newOrder.changeActiveAmount(tradeAmount);
            }
        }
    }

    public void cancelOrder(Long id) {
        int count = orderBookRepository.customDeleteById(id);
        if (count == 0)
            throw new IllegalArgumentException("이미 체결된 주문입니다");
    }

    private List<OrderBookDto> orderFilter(List<OrderBookDto> orderBookDtoList, Orders order) {
        return orderBookDtoList.stream()
                .filter(OrderBookDto -> OrderBookDto.getOrders() == order)
                .sorted(
                        (o1, o2) -> {
                            int compare = Double.compare(o2.getPrice(), o1.getPrice());
                            return compare == 0 ?
                                    (order == Orders.BUY ?
                                            o1.getCreatedDate().compareTo(o2.getCreatedDate()) :
                                            o2.getCreatedDate().compareTo(o1.getCreatedDate())) :
                                    compare;
                        }
                )
                .collect(Collectors.toList());
    }

    private List<OrderBook> orderProcessFilter(List<OrderBook> orderBookList, Orders order) {
        return orderBookList.stream()
                .sorted(
                        (o1, o2) -> {
                            int compare = order == Orders.BUY ?
                                    Double.compare(o1.getPrice(), o2.getPrice()) :
                                    Double.compare(o2.getPrice(), o1.getPrice());
                            return compare == 0 ?
                                    o1.getCreatedDate().compareTo(o2.getCreatedDate()) :
                                    compare;
                        }
                )
                .collect(Collectors.toList());
    }
}
