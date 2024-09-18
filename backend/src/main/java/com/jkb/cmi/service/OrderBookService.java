package com.jkb.cmi.service;

import com.jkb.cmi.dto.request.OrderRequest;
import com.jkb.cmi.dto.response.ActiveOrderResponse;
import com.jkb.cmi.dto.response.OrderBookResponse;
import com.jkb.cmi.entity.Currency;
import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.entity.type.Status;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.OrderBookRepository;
import com.jkb.cmi.repository.UserRepository;
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

    public List<OrderBookResponse> entryOrderBook(Long currencyId) {
        List<OrderBook> orderBookList = orderBookRepository.getByCurrency_Id(currencyId);

        List<OrderBook> buyOrders = OrderFilter(orderBookList, Orders.BUY);
        List<OrderBook> sellOrders = OrderFilter(orderBookList, Orders.SELL);
        sellOrders.addAll(buyOrders);
        return OrderBookResponse.tolist(sellOrders);
    }

    public List<OrderBookResponse> orderProcessing(OrderRequest orderRequest, Orders orders, OrderBook newOrder) {
        List<OrderBook> orderBookList = orderBookRepository.findByPriceForUpdate(
                orderRequest.getCurrencyId(), orderRequest.getPrice(), orders);
        orderBookRepository.save(newOrder);

        List<OrderBook> orderList = orderProcessFilter(orderBookList, orders);

        matchOrder(newOrder, orderList, orders);

        return entryOrderBook(orderRequest.getCurrencyId());
    }

    public List<ActiveOrderResponse> getActiveOrder(String username) {
        List<OrderBook> orderBookList = orderBookRepository.getActiveOrderByUsername(username)
                .stream().sorted(
                        Comparator.comparingLong(OrderBook::getId)
                ).toList();
        return ActiveOrderResponse.tolist(orderBookList);
    }

    public void addOrder(OrderRequest orderRequest, Orders orders) {
        User user = userRepository.getByUsername(orderRequest.getUsername());
        Currency currency = currencyRepository.getReferenceById(orderRequest.getCurrencyId());

        OrderBook newOrderBook = orderRequest.toEntity(user, currency, orders);

        List<OrderBookResponse> newOrderList = orderProcessing(orderRequest, orders, newOrderBook);
        sseService.sendEventToAll("orderBook " + orderRequest.getCurrencyId(), newOrderList);
    }

    public void matchOrder(OrderBook newOrder, List<OrderBook> orderList, Orders orders) {
        while (!orderList.isEmpty() &&
                (orders == Orders.BUY && newOrder.getPrice() >= orderList.get(0).getPrice() ||
                        orders == Orders.SELL && newOrder.getPrice() <= orderList.get(0).getPrice())) {

            OrderBook activeOrder = orderList.get(0);

            double tradeAmount = Math.min(newOrder.getActiveAmount(), activeOrder.getActiveAmount());

            double newOrderAmount = newOrder.getActiveAmount() - tradeAmount;
            double activeOrderAmount = activeOrder.getActiveAmount() - tradeAmount;

            tradeHistoryService.saveTradeHistory(newOrder, tradeAmount,
                    newOrderAmount == 0 ? Status.COMPLETE : Status.PARTIAL);
            tradeHistoryService.saveTradeHistory(activeOrder, tradeAmount,
                    activeOrderAmount == 0 ? Status.COMPLETE : Status.PARTIAL);


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

    private List<OrderBook> OrderFilter(List<OrderBook> orderBookList, Orders order) {
        return orderBookList.stream()
                .filter(OrderBook -> OrderBook.getOrders() == order)
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
