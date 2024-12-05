package com.jkb.cmi.asset.controller;

import com.jkb.cmi.asset.dto.CashAndCurrencyResponse;
import com.jkb.cmi.asset.dto.UserAssetResponse;
import com.jkb.cmi.asset.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/asset")
public class AssetController {
    private final AssetService assetService;
    @GetMapping
    ResponseEntity<UserAssetResponse> getUserAsset(String username) {
        UserAssetResponse userAssetResponse = assetService.getUserAsset(username);
        return ResponseEntity.ok(userAssetResponse);
    }

    @GetMapping("/cash-currency")
    ResponseEntity<CashAndCurrencyResponse> getCashAndCurrencyByUser(
            String username, String market) {
        CashAndCurrencyResponse cashAndCurrencyResponse =
                assetService.getCashAndCurrencyByUser(username, market);
        return ResponseEntity.ok(cashAndCurrencyResponse);
    }
}
