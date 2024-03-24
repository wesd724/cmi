package com.jkb.cmi.common;

import com.jkb.cmi.dto.APIResponseDto;
import com.jkb.cmi.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final String URL = "https://api.upbit.com/v1/ticker?markets=";
    private final String markets = "KRW-BTC, KRW-ETH";
    private final SseService sseService;

    @Scheduled(cron = "*/10 * * * * *")
    public void run() {
        sseService.send(getData());
    }

    private List<APIResponseDto> getData() {
        RestClient restClient = RestClient.create();

        List<APIResponseDto> res = restClient.get()
                .uri(URL + markets)
                .retrieve()
                .body(new ParameterizedTypeReference<List<APIResponseDto>>() {});

        return res;
    }
}
