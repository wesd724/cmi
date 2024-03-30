package com.jkb.cmi;

import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.repository.TradeHistoryRepository;
import com.jkb.cmi.service.TradeHistoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
public class TradeHistoryTest {
    @Autowired
    TradeHistoryRepository tradeHistoryRepository;
    @Autowired
    TradeHistoryService tradeHistoryService;

    @Test
    void test1() {
        List<TradeHistory> tradeHistories2 =
                tradeHistoryRepository.findByCompleteTrueAndCompleteDateNull();

        tradeHistories2.forEach(System.out::println);
    }

    @Test
    void test2() {
        tradeHistoryRepository.findByUsername("test");
    }

    @Test
    @Transactional
    @Rollback(false)
    void test3() {
        tradeHistoryRepository.buyTradeCompleteProcessing(1L, 100000000d);
        tradeHistoryRepository.sellTradeCompleteProcessing(1L, 100000000d);
    }

    @Test
    @Transactional
    @Rollback(false)
    void test5() {
        List<Long> id = List.of(1L, 100L);
        List<Double> price = List.of(100d, 200d);
        tradeHistoryRepository.test(id, price);

    }

}
