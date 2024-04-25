package com.jkb.cmi.controller;

import com.jkb.cmi.dto.request.UserRequest;
import com.jkb.cmi.dto.response.CashAndCurrencyResponse;
import com.jkb.cmi.dto.response.UserAssetResponse;
import com.jkb.cmi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @PostMapping("/signup")
    ResponseEntity<Void> signUp(@RequestBody UserRequest userRequest) {
        userService.signUp(userRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    ResponseEntity<Boolean> login(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }

    @GetMapping("/asset")
    ResponseEntity<UserAssetResponse> getUserAsset(String username) {
        UserAssetResponse userAssetResponse = userService.getUserAsset(username);
        return ResponseEntity.ok(userAssetResponse);
    }

    @GetMapping("/cash-currency")
    ResponseEntity<CashAndCurrencyResponse> getCashAndCurrencyByUser(
            String username, String market) {
        CashAndCurrencyResponse cashAndCurrencyResponse =
                userService.getCashAndCurrencyByUser(username, market);
        return ResponseEntity.ok(cashAndCurrencyResponse);
    }
}
