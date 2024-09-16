package com.jkb.cmi.service;

import com.jkb.cmi.entity.*;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.repository.CashAssetRepository;
import com.jkb.cmi.repository.CurrencyAssetRepository;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyAssetService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final CashAssetRepository cashAssetRepository;
    private final CurrencyAssetRepository currencyAssetRepository;
    private final NotificationService notificationService;

    public void updateCurrencyAsset(TradeHistory tradeHistory) {
        //System.out.println("updateCurrencyAsset의 트랜잭션 이름:" + TransactionSynchronizationManager.getCurrentTransactionName());
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
            if (currencyAsset.getAmount() != 0) {
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
        notificationService.saveNotification(tradeHistory); // this tradeHistory fk => s-lock
    }
}
