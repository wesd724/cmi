package com.jkb.cmi.orderbook.dto;

import com.jkb.cmi.common.enums.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderBookDto {
    private Orders orders;
    private Double activeAmount;
    private Double price;
    private LocalDateTime createdDate;
}
