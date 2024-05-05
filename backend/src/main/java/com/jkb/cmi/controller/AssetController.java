package com.jkb.cmi.controller;

import com.jkb.cmi.dto.response.CashAndCurrencyResponse;
import com.jkb.cmi.dto.response.UserAssetResponse;
import com.jkb.cmi.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
