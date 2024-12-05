package com.jkb.cmi.asset.dto;

import com.jkb.cmi.asset.entity.CashAsset;
import com.jkb.cmi.asset.entity.CurrencyAsset;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAssetResponse {
    private Long balance;
    private List<CurrencyAssetResponse> currencyAssetResponseList;

    public static UserAssetResponse of(CashAsset cashAsset, List<CurrencyAsset> currencyAssets) {
        return new UserAssetResponse(
                cashAsset.getBalance(),
                CurrencyAssetResponse.tolist(currencyAssets)
        );
    }
}
