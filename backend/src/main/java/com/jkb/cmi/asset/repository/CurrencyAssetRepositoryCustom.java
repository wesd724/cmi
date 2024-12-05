package com.jkb.cmi.asset.repository;

import com.jkb.cmi.external.dto.TickerAPIResponse;

import java.util.List;

public interface CurrencyAssetRepositoryCustom {
    void saveInitialCurrencyAsset(List<TickerAPIResponse> tickerApiResponses, Long userId);
}
