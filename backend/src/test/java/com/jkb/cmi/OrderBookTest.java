package com.jkb.cmi;

import com.jkb.cmi.repository.OrderBookRepository;
import com.jkb.cmi.service.OrderBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class OrderBookTest {

    @Autowired
    private OrderBookRepository orderBookRepository;
    @Autowired
    private OrderBookService orderBookService;

    @Test
    @Transactional
    @Rollback(false)
    void test() {
        System.out.println(orderBookRepository.customDeleteById(191L));
    }
}
