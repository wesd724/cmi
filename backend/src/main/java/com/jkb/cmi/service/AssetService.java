package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.CashAndCurrencyResponse;
import com.jkb.cmi.dto.response.UserAssetResponse;
import com.jkb.cmi.entity.CashAsset;
import com.jkb.cmi.entity.CurrencyAsset;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.repository.CashAssetRepository;
import com.jkb.cmi.repository.CurrencyAssetRepository;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetService {
    private final CashAssetRepository cashAssetRepository;
    private final CurrencyAssetRepository currencyAssetRepository;
    private final TradeHistoryRepository tradeHistoryRepository;

    @Transactional(readOnly = true)
    public UserAssetResponse getUserAsset(String username) {
        CashAsset cashAsset = cashAssetRepository.getByUser_Username(username);
        List<CurrencyAsset> currencyAssets = currencyAssetRepository.getByUser_Username(username);
        return UserAssetResponse.of(cashAsset, currencyAssets);
    }

    @Transactional(readOnly = true)
    public CashAndCurrencyResponse getCashAndCurrencyByUser(String username, String market) {
        List<TradeHistory> tradeHistories =
                tradeHistoryRepository.findByUsernameAndCompleteFalse(username);
        Double amountByNotComplete = totalAmountByNotComplete(tradeHistories, username, market);
        Long priceByNotComplete = totalPriceByNotComplete(tradeHistories, username);

        CashAsset cashAsset = cashAssetRepository.getByUser_Username(username);

        CashAndCurrencyResponse cashAndCurrencyResponse =
                currencyAssetRepository.findByUser_UsernameAndCurrency_market(username, market)
                        .map(currencyAsset -> new CashAndCurrencyResponse(
                                cashAsset.getBalance() - priceByNotComplete, currencyAsset.getAmount() - amountByNotComplete)
                        )
                        .orElseGet(() -> new CashAndCurrencyResponse(cashAsset.getBalance() - priceByNotComplete, 0d));
        return cashAndCurrencyResponse;
    }

    private Double totalAmountByNotComplete(List<TradeHistory> tradeHistories, String username, String market) {
        return tradeHistories.stream()
                .filter(tradeHistory -> tradeHistory.getCurrency().getMarket().equals(market))
                .map(tradeHistory -> tradeHistory.getAmount())
                .reduce(0D, Double::sum);
    }

    private Long totalPriceByNotComplete(List<TradeHistory> tradeHistories, String username) {
        return tradeHistories.stream()
                .map(tradeHistory -> Math.round(tradeHistory.getAmount() * tradeHistory.getPrice()))
                .reduce(0L, Long::sum);
    }

}
