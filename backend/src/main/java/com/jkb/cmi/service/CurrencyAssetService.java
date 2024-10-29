package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.APIResponse;
import com.jkb.cmi.entity.CashAsset;
import com.jkb.cmi.entity.CurrencyAsset;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.event.TradeHistoryEvent;
import com.jkb.cmi.event.UserSignUpEvent;
import com.jkb.cmi.repository.CashAssetRepository;
import com.jkb.cmi.repository.CurrencyAssetRepository;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyAssetService {
    private final CurrencyAssetRepository currencyAssetRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final CashAssetRepository cashAssetRepository;
    private final MarketDataService marketDataService;

    @Async("asyncEvent")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void initialCurrencyAsset(UserSignUpEvent event) {
        //System.out.println("트랜잭션 상태:" + TransactionSynchronizationManager.isActualTransactionActive());
        //System.out.println("initialCurrencyAsset의 트랜잭션 이름:" + TransactionSynchronizationManager.getCurrentTransactionName());
        List<APIResponse> apiResponses = marketDataService.getCurrentPrice();
        currencyAssetRepository.saveInitialCurrencyAsset(apiResponses, event.getUserId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateCurrencyAsset(TradeHistoryEvent event) {
        //System.out.println("updateCurrencyAsset의 트랜잭션 이름:" + TransactionSynchronizationManager.getCurrentTransactionName());
        TradeHistory tradeHistory = event.getTradeHistory();
        Long userId = tradeHistory.getUser().getId();
        Long currencyId = tradeHistory.getCurrency().getId();
        Double amount = tradeHistory.getAmount();
        Double price = tradeHistory.getPrice();
        long tradePrice = Math.round(price * amount);

        long buyPrice = tradeHistory.getOrders() == Orders.BUY ? tradePrice : -tradePrice;
        Double buyAmount = tradeHistory.getOrders() == Orders.BUY ? amount : -amount;

        try {
            CurrencyAsset currencyAsset = currencyAssetRepository
                    .getByUser_IdAndCurrency_Id(userId, currencyId)
                    .orElseThrow(IllegalArgumentException::new);

            currencyAsset.changeAmount(buyAmount);
            currencyAsset.changeBuyPrice(buyPrice);
            if (currencyAsset.getAmount() != 0) {
                currencyAsset.setAverageCurrencyBuyPrice();
            } else {
                currencyAssetRepository.deleteByCurrency(userId, currencyId);
            }

        } catch (IllegalArgumentException e) {
            CurrencyAsset currencyAsset = CurrencyAsset.builder()
                    .user(userRepository.getReferenceById(userId))
                    .currency(currencyRepository.getReferenceById(currencyId))
                    .buyPrice(buyPrice).averageCurrencyBuyPrice(price).amount(amount)
                    .build();
            currencyAssetRepository.save(currencyAsset);
        }

        CashAsset cashAsset = cashAssetRepository.getByUser_Id(userId);
        cashAsset.changeBalance(buyPrice);
    }
}
