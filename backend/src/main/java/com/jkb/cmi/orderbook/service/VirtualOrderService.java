package com.jkb.cmi.orderbook.service;

import com.jkb.cmi.common.enums.Orders;
import com.jkb.cmi.external.dto.OrderBookAPIResponse;
import com.jkb.cmi.orderbook.entity.OrderBook;
import com.jkb.cmi.orderbook.repository.OrderBookRepository;
import com.jkb.cmi.external.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VirtualOrderService {
    private final OrderBookRepository orderBookRepository;
    private final OrderBookService orderBookService;
    private final MarketDataService marketDataService;

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2.0)
    )
    public void generateOrder() {
        List<OrderBookAPIResponse> orderBookAPIResponseList =
                marketDataService.getRealOrderBookUnit();

        orderBookAPIResponseList.forEach(orderBookAPIResponse -> {
            long currencyId = orderBookAPIResponseList.indexOf(orderBookAPIResponse) + 1;
            OrderBookAPIResponse.Unit unit = orderBookAPIResponse.getOrderbook_units().get(0);

            addOrder(currencyId, unit);
        });
    }

    private void addOrder(long currencyId, OrderBookAPIResponse.Unit unit) {
        Random random = new Random();
        double ratio = random.nextInt(4) + 2; // 2 ~ 5
        orderBookService.addVirtualOrder(
                2L,
                currencyId,
                unit.getAsk_size() * (ratio / 10.0),
                unit.getAsk_price(),
                Orders.SELL);

        orderBookService.addVirtualOrder(
                2L,
                currencyId,
                unit.getBid_size() * (ratio / 10.0),
                unit.getBid_price(),
                Orders.BUY);

        orderBookService.sendOrderBook(currencyId);
    }

    @Transactional
    public void cleanUpOrder() {
        List<OrderBook> orderBookList = orderBookRepository.getByUser_Id(2L);
        orderBookList.forEach(orderBook -> {
            LocalDateTime activeOrderTime = orderBook.getCreatedDate();
            Duration duration = Duration.between(activeOrderTime, LocalDateTime.now());
            if (duration.toMinutes() >= 2L) {
                orderBookRepository.customDeleteById(orderBook.getId());
            }
        });
    }
}
