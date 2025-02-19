package com.jkb.cmi.orderbook.controller;

import com.jkb.cmi.orderbook.dto.request.OrderRequest;
import com.jkb.cmi.orderbook.dto.response.ActiveOrderResponse;
import com.jkb.cmi.orderbook.dto.response.OrderBookResponse;
import com.jkb.cmi.common.enums.Orders;
import com.jkb.cmi.orderbook.service.OrderBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderBookService orderBookService;

    @PostMapping("/buy")
    public ResponseEntity<Void> buy(@RequestBody OrderRequest orderRequest) {
        orderBookService.addOrder(orderRequest, Orders.BUY);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/sell")
    public ResponseEntity<Void> sell(@RequestBody OrderRequest orderRequest) {
        orderBookService.addOrder(orderRequest, Orders.SELL);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderBookResponse>> getOrderBook(Long currencyId) {
        List<OrderBookResponse> orderBookResponseList = orderBookService.getOrderBook(currencyId);
        return ResponseEntity.ok(orderBookResponseList);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ActiveOrderResponse>> getActiveOrder(String username) {
        List<ActiveOrderResponse> activeOrderResponseList = orderBookService.getActiveOrder(username);
        return ResponseEntity.ok(activeOrderResponseList);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelOrder(Long id) {
        orderBookService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
}
