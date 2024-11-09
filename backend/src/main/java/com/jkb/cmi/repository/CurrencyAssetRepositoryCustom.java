package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.TickerAPIResponse;

import java.util.List;

public interface CurrencyAssetRepositoryCustom {
    void saveInitialCurrencyAsset(List<TickerAPIResponse> tickerApiResponses, Long userId);
}
