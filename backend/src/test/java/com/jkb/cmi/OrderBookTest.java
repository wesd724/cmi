package com.jkb.cmi;

import com.jkb.cmi.dto.request.OrderRequest;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.OrderBookRepository;
import com.jkb.cmi.repository.UserRepository;
import com.jkb.cmi.service.OrderBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class OrderBookTest {

    @Autowired
    private OrderBookRepository orderBookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private OrderBookService orderBookService;

    @Test
    @Transactional
    @Rollback(false)
    void concurrentlyOrderTest() throws InterruptedException {
        OrderRequest orderRequest = new OrderRequest("test2", 4L, 17, 769.6);
        int count = 123;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            executorService.submit(() -> {
                try {
                    orderBookService.addOrder(orderRequest, Orders.SELL);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("FINISHED");
    }
}
