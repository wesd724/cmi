package com.jkb.cmi.common;

import com.jkb.cmi.repository.OrderBookRepository;
import com.jkb.cmi.service.MarketDataService;
import com.jkb.cmi.service.RecommendationService;
import com.jkb.cmi.service.SseService;
import com.jkb.cmi.service.VirtualOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final OrderBookRepository orderBookRepository;
    private final SseService sseService;
    private final RecommendationService recommendationService;
    private final MarketDataService marketDataService;
    private final VirtualOrderService virtualOrderService;

    @Scheduled(fixedDelay = 10000)
    public void notification() {
        sseService.sendNotificationToAll();
    }

    @Scheduled(fixedDelay = 5 * 1000, initialDelay = 5 * 1000)
    public void addVirtualOrder() {
        virtualOrderService.generateOrder();
    }

    @Transactional
    @Scheduled(fixedDelay = 3 * 60 * 1000, initialDelay = 3 * 60 * 1000)
    public void cleanUpVirtualOrder() {
        orderBookRepository.deleteExpiredVirtualOrder(3);
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void recommendation() {
        recommendationService.updateRecommendation(marketDataService.getPredictPrice());
    }
}
