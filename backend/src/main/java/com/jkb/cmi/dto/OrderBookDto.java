package com.jkb.cmi.dto;

import com.jkb.cmi.entity.type.Orders;
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
