package com.jkb.cmi.service;

import com.jkb.cmi.entity.*;
import com.jkb.cmi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyAssetService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final CashAssetRepository cashAssetRepository;
    private final CurrencyAssetRepository currencyAssetRepository;
    private final TradeHistoryRepository tradeHistoryRepository;

    public void getCurrencyAsset(Long userId) {

    }

    public void updateCurrencyAsset() {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByCompleteTrueAndCompleteDateNull();
        for(TradeHistory tradeHistory: tradeHistories) {
            Long userId = tradeHistory.getUser().getId();
            Long currencyId = tradeHistory.getCurrency().getId();
            String a = tradeHistory.getCurrency().getName();
            Double amount = tradeHistory.getAmount();
            Double price = tradeHistory.getPrice();
            long buyPrice = Math.round(price * amount);

            User user = userRepository.getReferenceById(userId);
            Currency currency = currencyRepository.getReferenceById(currencyId);

            CashAsset cashAsset = cashAssetRepository.getByUser_Id(userId);
            cashAsset.changeBalance(buyPrice);

            try {
                CurrencyAsset currencyAsset = currencyAssetRepository
                        .getByUser_IdAndCurrency_Id(userId, currencyId).orElseThrow(IllegalArgumentException::new);

                currencyAsset.changeAmount(amount);
                currencyAsset.changeBuyPrice(buyPrice);
                currencyAsset.setAverageCurrencyBuyPrice();

            } catch(IllegalArgumentException e) {
                CurrencyAsset currencyAsset = CurrencyAsset.builder()
                        .user(user).currency(currency)
                        .buyPrice(buyPrice).averageCurrencyBuyPrice(price).amount(amount).build();
                currencyAssetRepository.save(currencyAsset);
            } finally {
                tradeHistory.complete();
            }
        }
    }
}
