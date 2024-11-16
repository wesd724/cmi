package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.OrderBookAPIResponse;
import com.jkb.cmi.dto.response.RecommendationResponse;
import com.jkb.cmi.dto.response.TickerAPIResponse;
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

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2.0)
    )
    public List<OrderBookAPIResponse> getRealOrderBookUnit() {
        RestClient restClient = RestClient.create();

        List<OrderBookAPIResponse> res = restClient.get()
                .uri(orderBookURL + markets)
                .retrieve()
                .body(new ParameterizedTypeReference<List<OrderBookAPIResponse>>() {
                });

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
