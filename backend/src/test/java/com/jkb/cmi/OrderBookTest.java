package com.jkb.cmi;

import com.jkb.cmi.service.OrderBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderBookTest {

    @Autowired
    private OrderBookService orderBookService;

    @Test
    void test() {

    }
}
