package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.CashAndCurrencyResponse;
import com.jkb.cmi.dto.response.UserAssetResponse;
import com.jkb.cmi.entity.CashAsset;
import com.jkb.cmi.entity.CurrencyAsset;
import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.repository.CashAssetRepository;
import com.jkb.cmi.repository.CurrencyAssetRepository;
import com.jkb.cmi.repository.OrderBookRepository;
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
    private final OrderBookRepository orderBookRepository;

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

}
