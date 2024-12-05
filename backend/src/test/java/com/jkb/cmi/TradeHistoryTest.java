package com.jkb.cmi;

import com.jkb.cmi.tradehistory.entity.TradeHistory;
import com.jkb.cmi.tradehistory.entity.type.Status;
import com.jkb.cmi.tradehistory.repository.TradeHistoryRepository;
import com.jkb.cmi.tradehistory.service.TradeHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
                tradeHistoryRepository.findByStatusAndCompleteDateNull(Status.COMPLETE);

        tradeHistories2.forEach(System.out::println);
    }

    @Test
    void test2() {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUsername("asd");
        System.out.println(tradeHistories);
    }

    @Test
    void test5() {
    }

}
