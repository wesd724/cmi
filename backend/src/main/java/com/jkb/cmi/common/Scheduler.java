package com.jkb.cmi.common;

import com.jkb.cmi.service.MarketDataService;
import com.jkb.cmi.service.RecommendationService;
import com.jkb.cmi.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final SseService sseService;
    private final RecommendationService recommendationService;
    private final MarketDataService marketDataService;

    @Transactional
    @Scheduled(cron = "*/10 * * * * *")
    public void notification() {
        sseService.sendNotificationToAll();
    }

    @Transactional
    @Scheduled(cron = "0 1 0 * * *")
    public void recommendation() {
        recommendationService.updateRecommendation(marketDataService.getPredictPrice());
    }
}
