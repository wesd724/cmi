package com.jkb.cmi.common;

import com.jkb.cmi.dto.APIResponseDto;
import com.jkb.cmi.service.CurrencyAssetService;
import com.jkb.cmi.service.SseService;
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
    @Value("${upbit.url}")
    private String URL;
    @Value("${upbit.markets}")
    private String markets;


    @Transactional
    @Scheduled(cron = "*/10 * * * * *")
    public void run() {
        tradeHistoryService.completeProcess(getData());
        currencyAssetService.updateCurrencyAsset();
        sseService.send(1);
    }

    private List<APIResponseDto> getData() {
        RestClient restClient = RestClient.create();

        List<APIResponseDto> res = restClient.get()
                .uri(URL + markets)
                .retrieve()
                .body(new ParameterizedTypeReference<List<APIResponseDto>>() {
                });

        return res;
    }
}
