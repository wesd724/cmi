package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.TradeHistoryResponse;
import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.type.Status;
import com.jkb.cmi.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeHistoryService {
    private final TradeHistoryRepository tradeHistoryRepository;
    private final CurrencyAssetService currencyAssetService;

    @Transactional(readOnly = true)
    public List<TradeHistoryResponse> getTradeHistory(String username) {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUsername(username);
        return TradeHistoryResponse.tolist(tradeHistories);
    }

    public void saveTradeHistory(OrderBook orderBook, double amount, Status status) {
        //System.out.println("saveTradeHistory의 트랜잭션 이름:" + TransactionSynchronizationManager.getCurrentTransactionName());
        TradeHistory tradeHistory = TradeHistory.builder()
                .user(orderBook.getUser()).currency(orderBook.getCurrency())
                .orders(orderBook.getOrders()).amount(amount)
                .price(orderBook.getPrice())
                .orderDate(orderBook.getCreatedDate()).completeDate(LocalDateTime.now())
                .status(status)
                .build();

        tradeHistoryRepository.save(tradeHistory); // this tradeHistory insert => x-lock
        currencyAssetService.updateCurrencyAsset(tradeHistory);
    }
}
