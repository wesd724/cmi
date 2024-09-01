package com.jkb.cmi.service;

import com.jkb.cmi.entity.*;
import com.jkb.cmi.entity.type.Orders;
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
    private final NotificationRepository notificationRepository;

    public void updateCurrencyAsset(TradeHistory tradeHistory) {
        Long userId = tradeHistory.getUser().getId();
        Long currencyId = tradeHistory.getCurrency().getId();
        Double amount = tradeHistory.getAmount();
        Double price = tradeHistory.getPrice();
        long tradePrice = Math.round(price * amount);

        long buyPrice = tradeHistory.getOrders() == Orders.BUY ? tradePrice : -tradePrice;
        Double buyAmount = tradeHistory.getOrders() == Orders.BUY ? amount : -amount;

        CashAsset cashAsset = cashAssetRepository.getByUser_Id(userId);
        cashAsset.changeBalance(buyPrice);
        try {
            CurrencyAsset currencyAsset = currencyAssetRepository
                    .getByUser_IdAndCurrency_Id(userId, currencyId).orElseThrow(IllegalArgumentException::new);

            currencyAsset.changeAmount(buyAmount);
            currencyAsset.changeBuyPrice(buyPrice);
            if (currencyAsset.getAmount() != 0D) {
                currencyAsset.setAverageCurrencyBuyPrice();
            } else {
                currencyAssetRepository.deleteByCurrency(userId, currencyId);
            }

        } catch (IllegalArgumentException e) {
            User user = userRepository.getReferenceById(userId);
            Currency currency = currencyRepository.getReferenceById(currencyId);

            CurrencyAsset currencyAsset = CurrencyAsset.builder()
                    .user(user).currency(currency)
                    .buyPrice(buyPrice).averageCurrencyBuyPrice(price).amount(amount).build();
            currencyAssetRepository.save(currencyAsset);
        }
        notificationRepository.saveAllNotification(List.of(tradeHistory));
    }
}
