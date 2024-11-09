package com.jkb.cmi;

import com.jkb.cmi.dto.response.OrderBookAPIResponse;
import com.jkb.cmi.service.MarketDataService;
import com.jkb.cmi.service.VirtualOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VirtualOrderTest {
    @Autowired
    VirtualOrderService virtualOrderService;

    @Autowired
    MarketDataService marketDataService;

    @Test
    void test1() {
        virtualOrderService.generateOrder();
    }

    @Test
    void test() {
        List<OrderBookAPIResponse> or = marketDataService.getRealOrderBookUnit();
        or.forEach(o -> {
            System.out.println(o.getMarket());
            OrderBookAPIResponse.Unit unit = o.getOrderbook_units().get(0);
            System.out.println(unit.getAsk_price());
            System.out.println(unit.getAsk_size());
            System.out.println(unit.getBid_price());
            System.out.println(unit.getBid_size());
        });
    }
}
