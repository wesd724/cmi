package com.jkb.cmi.common;

import com.jkb.cmi.dto.response.APIResponse;
import com.jkb.cmi.dto.response.RecommendationResponse;
import com.jkb.cmi.service.CurrencyAssetService;
import com.jkb.cmi.service.SseService;
import com.jkb.cmi.service.RecommendationService;
import com.jkb.cmi.service.TradeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final SseService sseService;
    private final CurrencyAssetService currencyAssetService;
    private final TradeHistoryService tradeHistoryService;
    private final RecommendationService recommendationService;
    @Value("${upbit.url}")
    private String URL;
    @Value("${upbit.markets}")
    private String markets;

    @Value("${predict.url}")
    private String predictURL;


    @Transactional
    //@Scheduled(cron = "*/10 * * * * *")
    public void notification() {
        sseService.sendNotificationToAll();
    }

    @Transactional
    @Scheduled(cron = "0 1 0 * * *")
    public void recommendation() {
        recommendationService.updateRecommendation(getPredict());
    }

    private List<APIResponse> getData() {
        RestClient restClient = RestClient.create();

        List<APIResponse> res = restClient.get()
                .uri(URL + markets)
                .retrieve()
                .body(new ParameterizedTypeReference<List<APIResponse>>() {
                });

        return res;
    }

    private List<RecommendationResponse> getPredict() {
        RestClient restClient = RestClient.create();

        List<RecommendationResponse> res = restClient.get()
                .uri(predictURL)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RecommendationResponse>>() {
                });

        return res;
    }
}
