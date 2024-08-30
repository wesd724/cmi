package com.jkb.cmi.service;

import com.jkb.cmi.dto.request.OrderRequest;
import com.jkb.cmi.dto.response.OrderBookResponse;
import com.jkb.cmi.entity.Currency;
import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.entity.type.Status;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.OrderBookRepository;
import com.jkb.cmi.repository.TradeHistoryRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderBookService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final TradeHistoryRepository tradeHistoryRepository;
    private final OrderBookRepository orderBookRepository;
    private final SseService sseService;

    public List<OrderBookResponse> getOrders(Long currencyId) {
        List<OrderBook> orderBookList = orderBookRepository.getByCurrency_Id(currencyId);
        List<OrderBook> buyOrders = OrderFilter(orderBookList, Orders.BUY);
        List<OrderBook> sellOrders = OrderFilter(orderBookList, Orders.SELL);
        if (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            //matchOrder(buyOrders, sellOrders);
        }
        sellOrders.addAll(buyOrders);
        return OrderBookResponse.tolist(sellOrders);
    }

    public void addOrder(OrderRequest orderRequest, Orders orders) {
        User user = userRepository.getByUsername(orderRequest.getUsername());
        Currency currency = currencyRepository.getReferenceById(orderRequest.getCurrencyId());

        OrderBook orderBook = orderRequest.toEntity(user, currency, orders);
        orderBookRepository.save(orderBook);

        sendOrderBook(orderRequest.getCurrencyId());
    }

    public void sendOrderBook(Long currencyId) {
        List<OrderBookResponse> orderBook = getOrders(currencyId);
        sseService.sendEventToAll("orderBook " + currencyId, orderBook);
    }

    public void matchOrder(List<OrderBook> buyOrders, List<OrderBook> sellOrders) {
        OrderBook highestBuy = buyOrders.get(0);
        OrderBook lowestSell = sellOrders.get(sellOrders.size() - 1);
        if (highestBuy.getPrice() >= lowestSell.getPrice()) {

            if (highestBuy.getActiveAmount() >= lowestSell.getActiveAmount()) {
                highestBuy.changeActiveAmount(lowestSell.getActiveAmount());

                if (highestBuy.getActiveAmount() == 0) {
                    orderBookRepository.delete(highestBuy);
                    saveTradeHistory(highestBuy, highestBuy.getOriginalAmount(), Status.COMPLETE);

                    buyOrders.remove(highestBuy);
                } else {
                    saveTradeHistory(highestBuy, lowestSell.getActiveAmount(), Status.PARTIAL);
                }

                orderBookRepository.delete(lowestSell);
                saveTradeHistory(lowestSell, lowestSell.getOriginalAmount(), Status.COMPLETE);

                sellOrders.remove(lowestSell);
            } else {
                lowestSell.changeActiveAmount(highestBuy.getActiveAmount());

                orderBookRepository.delete(highestBuy);
                saveTradeHistory(lowestSell, highestBuy.getActiveAmount(), Status.PARTIAL);
                saveTradeHistory(highestBuy, highestBuy.getOriginalAmount(), Status.COMPLETE);

                buyOrders.remove(highestBuy);
            }
        }
    }

    private List<OrderBook> OrderFilter(List<OrderBook> orderBookList, Orders order) {
        return orderBookList.stream()
                .filter(OrderBook -> OrderBook.getOrders() == order)
                .sorted(
                        (o1, o2) -> {
                            int compare = Double.compare(o2.getPrice(), o1.getPrice());
                            return compare == 0 ?
                                    o1.getCreatedDate().compareTo(o2.getCreatedDate()) :
                                    compare;
                        }
                )
                .collect(Collectors.toList());
    }

    private void saveTradeHistory(OrderBook orderBook, double amount, Status status) {
        TradeHistory tradeHistory = TradeHistory.builder()
                .user(orderBook.getUser()).currency(orderBook.getCurrency())
                .orders(orderBook.getOrders()).amount(amount)
                .price(orderBook.getPrice())
                .orderDate(orderBook.getCreatedDate()).completeDate(LocalDateTime.now())
                .status(status)
                .build();

        tradeHistoryRepository.save(tradeHistory);
    }
}
