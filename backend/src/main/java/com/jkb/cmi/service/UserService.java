package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.CashAndCurrencyResponse;
import com.jkb.cmi.dto.response.UserAssetResponse;
import com.jkb.cmi.dto.request.UserRequest;
import com.jkb.cmi.entity.CashAsset;
import com.jkb.cmi.entity.CurrencyAsset;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.repository.CashAssetRepository;
import com.jkb.cmi.repository.CurrencyAssetRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CashAssetRepository cashAssetRepository;
    private final CurrencyAssetRepository currencyAssetRepository;

    public void signUp(UserRequest userRequest) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();
        userRepository.save(user);

        CashAsset cashAsset = CashAsset.builder()
                .balance(100000000L).user(user)
                .build();
        cashAssetRepository.save(cashAsset);
    }

    public Boolean login(UserRequest userRequest) {
        try {
            userRepository.findByUsernameAndPassword(userRequest.getUsername(), userRequest.getPassword())
                    .orElseThrow(IllegalArgumentException::new);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public UserAssetResponse getUserAsset(String username) {
        CashAsset cashAsset = cashAssetRepository.getByUser_Username(username);
        List<CurrencyAsset> currencyAssets = currencyAssetRepository.getByUser_Username(username);
        return UserAssetResponse.of(cashAsset, currencyAssets);
    }

    @Transactional(readOnly = true)
    public CashAndCurrencyResponse getCashAndCurrencyByUser(String username, String market) {
        CashAsset cashAsset = cashAssetRepository.getByUser_Username(username);
        try {
            CurrencyAsset currencyAsset =
                    currencyAssetRepository.findByUser_UsernameAndCurrency_market(username, market)
                            .orElseThrow(IllegalArgumentException::new);

            return CashAndCurrencyResponse.of(cashAsset, currencyAsset);
        } catch(IllegalArgumentException e) {
            return new CashAndCurrencyResponse(cashAsset.getBalance(), 0d);
        }
    }
}
