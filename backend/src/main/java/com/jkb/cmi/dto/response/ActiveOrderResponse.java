package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.type.Orders;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActiveOrderResponse {
    private Long id;
    private String market;
    private String currencyName;
    private Orders orders;
    private Double originalAmount;
    private Double activeAmount;
    private Long price;
    private LocalDateTime createdDate;

    public static ActiveOrderResponse from(OrderBook orderBook) {
        Double amount = orderBook.getOriginalAmount();
        Double price = orderBook.getPrice();
        long tradePrice = Math.round(amount * price);
        return new ActiveOrderResponse(
                orderBook.getId(),
                orderBook.getCurrency().getMarket(),
                orderBook.getCurrency().getName(),
                orderBook.getOrders(),
                orderBook.getOriginalAmount(),
                orderBook.getActiveAmount(),
                tradePrice,
                orderBook.getCreatedDate()
        );
    }

    public static List<ActiveOrderResponse> tolist(List<OrderBook> orderBookList) {
        return orderBookList.stream()
                .map(ActiveOrderResponse::from)
                .toList();
    }
}
