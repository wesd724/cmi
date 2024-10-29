package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.APIResponse;
import com.jkb.cmi.dto.response.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MarketDataService {
    @Value("${upbit.url}")
    private String URL;
    @Value("${upbit.markets}")
    private String markets;
    @Value("${predict.url}")
    private String predictURL;

    public List<APIResponse> getCurrentPrice() {
        RestClient restClient = RestClient.create();

        List<APIResponse> res = restClient.get()
                .uri(URL + markets)
                .retrieve()
                .body(new ParameterizedTypeReference<List<APIResponse>>() {
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
