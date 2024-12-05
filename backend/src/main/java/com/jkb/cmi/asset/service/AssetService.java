package com.jkb.cmi.asset.service;

import com.jkb.cmi.asset.repository.CashAssetRepository;
import com.jkb.cmi.asset.repository.CurrencyAssetRepository;
import com.jkb.cmi.currency.repository.CurrencyRepository;
import com.jkb.cmi.asset.dto.CashAndCurrencyResponse;
import com.jkb.cmi.asset.dto.UserAssetResponse;
import com.jkb.cmi.asset.entity.CashAsset;
import com.jkb.cmi.asset.entity.CurrencyAsset;
import com.jkb.cmi.orderbook.entity.OrderBook;
import com.jkb.cmi.orderbook.repository.OrderBookRepository;
import com.jkb.cmi.tradehistory.entity.TradeHistory;
import com.jkb.cmi.common.enums.Orders;
import com.jkb.cmi.event.TradeHistoryEvent;
import com.jkb.cmi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetService {
    private final CashAssetRepository cashAssetRepository;
    private final CurrencyAssetRepository currencyAssetRepository;
    private final OrderBookRepository orderBookRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional(readOnly = true)
    public UserAssetResponse getUserAsset(String username) {
        CashAsset cashAsset = cashAssetRepository.getByUser_Username(username);
        List<CurrencyAsset> currencyAssets = currencyAssetRepository.getByUser_Username(username);
        return UserAssetResponse.of(cashAsset, currencyAssets);
    }

    @Transactional(readOnly = true)
    public CashAndCurrencyResponse getCashAndCurrencyByUser(String username, String market) {
        List<OrderBook> orderBookList = orderBookRepository.getActiveOrderByUsername(username);
        Double amountByNotComplete = totalAmountByNotComplete(orderBookList, market);
        Long priceByNotComplete = totalPriceByNotComplete(orderBookList);

        CashAsset cashAsset = cashAssetRepository.getByUser_Username(username);

        CashAndCurrencyResponse cashAndCurrencyResponse =
                currencyAssetRepository.findByUser_UsernameAndCurrency_market(username, market)
                        .map(currencyAsset -> new CashAndCurrencyResponse(
                                cashAsset.getBalance() - priceByNotComplete, currencyAsset.getAmount() - amountByNotComplete)
                        )
                        .orElseGet(() -> new CashAndCurrencyResponse(cashAsset.getBalance() - priceByNotComplete, 0d));
        return cashAndCurrencyResponse;
    }

    private Double totalAmountByNotComplete(List<OrderBook> orderBookList, String market) {
        return orderBookList.stream()
                .filter(orderBook -> orderBook.getOrders() == Orders.SELL && orderBook.getCurrency().getMarket().equals(market))
                .map(orderBook -> orderBook.getActiveAmount())
                .reduce(0D, Double::sum);
    }

    private Long totalPriceByNotComplete(List<OrderBook> orderBookList) {
        return orderBookList.stream()
                .filter(orderBook -> orderBook.getOrders() == Orders.BUY)
                .map(orderBook -> Math.round(orderBook.getActiveAmount() * orderBook.getPrice()))
                .reduce(0L, Long::sum);
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
