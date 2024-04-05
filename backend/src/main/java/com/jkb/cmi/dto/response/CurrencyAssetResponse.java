package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.CurrencyAsset;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyAssetResponse {
    private String market;
    private String currencyName;
    private Double amount;
    private Double averageCurrencyBuyPrice;
    private Long buyPrice;

    public static CurrencyAssetResponse from(CurrencyAsset currencyAsset) {
        return new CurrencyAssetResponse(
                currencyAsset.getCurrency().getMarket(),
                currencyAsset.getCurrency().getName(),
                currencyAsset.getAmount(),
                currencyAsset.getAverageCurrencyBuyPrice(),
                currencyAsset.getBuyPrice()
        );
    }

    public static List<CurrencyAssetResponse> tolist(List<CurrencyAsset> currencyAssets) {
        return currencyAssets.stream()
                .map(CurrencyAssetResponse::from)
                .toList();
    }
}
