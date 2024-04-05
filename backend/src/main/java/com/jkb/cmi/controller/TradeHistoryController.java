package com.jkb.cmi.controller;

import com.jkb.cmi.dto.request.OrderRequest;
import com.jkb.cmi.dto.response.TradeHistoryResponse;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.service.TradeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeHistoryController {
    private final TradeHistoryService tradeHistoryService;

    @GetMapping
    public ResponseEntity<List<TradeHistoryResponse>> getTradeHistory(String username) {
        List<TradeHistoryResponse> tradeHistoryResponses = tradeHistoryService.getTradeHistory(username);
        return ResponseEntity.ok(tradeHistoryResponses);
    }
    @PostMapping
    public void buy(@RequestBody OrderRequest orderRequest) {
        tradeHistoryService.order(orderRequest, Orders.BUY);
    }
    @DeleteMapping
    public void sell(@RequestBody OrderRequest orderRequest) {
        tradeHistoryService.order(orderRequest, Orders.SELL);
    }

    @DeleteMapping("/cancel")
    public void cancel(@RequestBody OrderRequest orderRequest) {

    }
}
