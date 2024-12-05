package com.jkb.cmi.orderbook.dto.response;

import com.jkb.cmi.orderbook.dto.OrderBookDto;
import com.jkb.cmi.common.enums.Orders;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return Objects.equals(price, tuple.price) &&
                    orders == tuple.orders;
        }

        @Override
        public int hashCode() {
            return Objects.hash(price, orders);
        }
    }

    public static List<OrderBookResponse> tolist(List<OrderBookDto> orderBookDtoList) {
        return orderBookDtoList.stream()
                .collect(Collectors.groupingBy(
                        orderBookDto -> new Tuple(orderBookDto.getPrice(), orderBookDto.getOrders()),
                        LinkedHashMap::new,
                        Collectors.summingDouble(OrderBookDto::getActiveAmount)
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
