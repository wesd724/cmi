package com.jkb.cmi.external.service;

import com.jkb.cmi.external.dto.OrderBookAPIResponse;
import com.jkb.cmi.recommendation.dto.RecommendationResponse;
import com.jkb.cmi.external.dto.TickerAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MarketDataService {
    @Value("${upbit.ticker.url}")
    private String tickerURL;
    @Value("${upbit.orderbook.url}")
    private String orderBookURL;
    @Value("${upbit.markets}")
    private String markets;
    @Value("${predict.url}")
    private String predictURL;


    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2.0)
    )
    public List<TickerAPIResponse> getCurrentPrice() {
        RestClient restClient = RestClient.create();

        List<TickerAPIResponse> res = restClient.get()
                .uri(tickerURL + markets)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TickerAPIResponse>>() {
                });

        return res;
    }

    public List<OrderBookAPIResponse> getRealOrderBookUnit() {
        RestClient restClient = RestClient.create();
        System.out.println("RestClient.create() 이후 restClient 사용 시작");
        long start = System.currentTimeMillis();
        List<OrderBookAPIResponse> res = restClient.get()
                .uri(orderBookURL + markets)
                .retrieve()
                .body(new ParameterizedTypeReference<List<OrderBookAPIResponse>>() {
                });
        long end = System.currentTimeMillis();
        System.out.println("외부 API 호출 이후 시간: " + (end - start) / 1000.0 + "ms");
        return res;
    }

    public List<RecommendationResponse> getPredictPrice() {
        RestClient restClient = RestClient.create();

        List<RecommendationResponse> res = restClient.get()
                .uri(predictURL)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RecommendationResponse>>() {
                });

        return res;
    }
}
