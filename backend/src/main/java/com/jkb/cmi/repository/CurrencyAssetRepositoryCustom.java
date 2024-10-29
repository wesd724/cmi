package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.APIResponse;

import java.util.List;

public interface CurrencyAssetRepositoryCustom {
    void saveInitialCurrencyAsset(List<APIResponse> apiResponses, Long userId);
}
