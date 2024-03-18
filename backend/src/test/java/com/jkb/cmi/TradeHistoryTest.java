package com.jkb.cmi;

import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.repository.TradeHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TradeHistoryTest {
    @Autowired
    TradeHistoryRepository tradeHistoryRepository;

    @Test
    void test1() {
        List<TradeHistory> tradeHistories =
                tradeHistoryRepository.findByPriceLessThanEqualAndCompleteFalse(80000000d);

        tradeHistories.forEach(System.out::println);

        List<TradeHistory> tradeHistories2 =
                tradeHistoryRepository.findByCompleteTrueAndCompleteDateNull();

        tradeHistories2.forEach(System.out::println);
    }
}
