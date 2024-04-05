package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.TradeHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TradeHistoryResponse {
    private String currencyName;
    private String order;
    private Double amount;
    private Double price;
    private Long tradePrice;
    private LocalDateTime orderDate;
    private LocalDateTime completeDate;

    public static TradeHistoryResponse from(TradeHistory tradeHistory) {
        Double amount = tradeHistory.getAmount();
        Double price = tradeHistory.getPrice();
        Long tradePrice = Math.round(amount * price);
        return new TradeHistoryResponse(
                tradeHistory.getCurrency().getName(),
                tradeHistory.getOrders().name(),
                tradeHistory.getAmount(),
                tradeHistory.getPrice(),
                tradePrice,
                tradeHistory.getCreatedDate(),
                tradeHistory.getCompleteDate()
        );
    }

    public static List<TradeHistoryResponse> tolist(List<TradeHistory> tradeHistories) {
        return tradeHistories.stream()
                .map(TradeHistoryResponse::from)
                .toList();
    }
}
