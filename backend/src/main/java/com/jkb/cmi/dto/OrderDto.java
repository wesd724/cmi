package com.jkb.cmi.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDto {
    private Long userId;
    private Long currencyId;
    private double amount;
    private double price;
}
