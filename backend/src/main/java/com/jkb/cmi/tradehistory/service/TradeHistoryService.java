package com.jkb.cmi.tradehistory.service;

import com.jkb.cmi.tradehistory.dto.TradeHistoryResponse;
import com.jkb.cmi.orderbook.entity.OrderBook;
import com.jkb.cmi.tradehistory.entity.TradeHistory;
import com.jkb.cmi.tradehistory.entity.type.Status;
import com.jkb.cmi.event.TradeHistoryEvent;
import com.jkb.cmi.tradehistory.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeHistoryService {
    private final TradeHistoryRepository tradeHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<TradeHistoryResponse> getTradeHistory(String username) {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUsername(username);
        return TradeHistoryResponse.tolist(tradeHistories);
    }

    public void saveTradeHistory(OrderBook orderBook, double price, double amount, Status status) {
        //System.out.println("saveTradeHistory의 트랜잭션 이름:" + TransactionSynchronizationManager.getCurrentTransactionName());
        TradeHistory tradeHistory = TradeHistory.builder()
                .user(orderBook.getUser()).currency(orderBook.getCurrency())
                .orders(orderBook.getOrders()).amount(amount)
                .price(price)
                .orderDate(orderBook.getCreatedDate()).completeDate(LocalDateTime.now())
                .status(status)
                .build();

        tradeHistoryRepository.save(tradeHistory); // this tradeHistory insert => x-lock
        eventPublisher.publishEvent(new TradeHistoryEvent(tradeHistory));
    }
}
