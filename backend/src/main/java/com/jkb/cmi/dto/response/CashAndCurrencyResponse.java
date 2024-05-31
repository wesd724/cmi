package com.jkb.cmi.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CashAndCurrencyResponse {
    private Long balance;
    private Double CurrencyAmount;
}
