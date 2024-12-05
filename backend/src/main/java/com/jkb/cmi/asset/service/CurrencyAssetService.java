package com.jkb.cmi.asset.service;

import com.jkb.cmi.external.dto.TickerAPIResponse;
import com.jkb.cmi.external.service.MarketDataService;
import com.jkb.cmi.event.UserSignUpEvent;
import com.jkb.cmi.asset.repository.CurrencyAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyAssetService {
    private final CurrencyAssetRepository currencyAssetRepository;
    private final MarketDataService marketDataService;

    @Async("asyncEvent")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void initialCurrencyAsset(UserSignUpEvent event) {
        //System.out.println("트랜잭션 상태:" + TransactionSynchronizationManager.isActualTransactionActive());
        //System.out.println("initialCurrencyAsset의 트랜잭션 이름:" + TransactionSynchronizationManager.getCurrentTransactionName());
        List<TickerAPIResponse> tickerApiRespons = marketDataService.getCurrentPrice();
        currencyAssetRepository.saveInitialCurrencyAsset(tickerApiRespons, event.getUserId());
    }


}
