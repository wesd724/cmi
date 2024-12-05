package com.jkb.cmi.external.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TickerAPIResponse {
    String market;
    Double trade_price;
}
