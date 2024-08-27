package com.jkb.cmi.controller;

import com.jkb.cmi.dto.request.OrderRequest;
import com.jkb.cmi.dto.response.OrderBookResponse;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.service.OrderBookService;
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

    @GetMapping
    public ResponseEntity<List<OrderBookResponse>> getOrderBook(Long currencyId) {
        List<OrderBookResponse> orderBookResponseList = orderBookService.getOrders(currencyId);
        return ResponseEntity.ok(orderBookResponseList);
    }
}
