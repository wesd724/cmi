package com.jkb.cmi.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderBookAPIResponse {
    private String market;
    private List<Unit> orderbook_units;

    @Getter
    public static class Unit {
        private double ask_price;
        private double bid_price;
        private double ask_size;
        private double bid_size;
    }
}
