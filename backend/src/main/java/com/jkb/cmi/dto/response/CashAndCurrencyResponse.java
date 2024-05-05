package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.CashAsset;
import com.jkb.cmi.entity.CurrencyAsset;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CashAndCurrencyResponse {
    private Long balance;
    private Double CurrencyAmount;
}
