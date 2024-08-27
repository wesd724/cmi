package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.type.Orders;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderBookResponse {
    private Double price;
    private Orders orders;
    private Double activeAmount;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Tuple {
        private Double price;
        private Orders orders;
    }

    public static List<OrderBookResponse> tolist(List<OrderBook> orderBookList) {
        return orderBookList.stream()
                .collect(Collectors.groupingBy(
                        orderBook -> new Tuple(orderBook.getPrice(), orderBook.getOrders()),
                        LinkedHashMap::new,
                        Collectors.summingDouble(OrderBook::getActiveAmount)
                )) // Map<Tuple, Double>
                .entrySet().stream()
                .map(entry -> new OrderBookResponse(
                        entry.getKey().getPrice(),
                        entry.getKey().getOrders(),
                        entry.getValue()
                ))
                .toList();
    }
}
