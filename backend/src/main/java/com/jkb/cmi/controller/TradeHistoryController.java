package com.jkb.cmi.controller;

import com.jkb.cmi.dto.OrderDto;
import com.jkb.cmi.dto.TradeHistoryDto;
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
    public ResponseEntity<List<TradeHistoryDto>> getTradeHistory(String username) {
        List<TradeHistoryDto> tradeHistoryDtos = tradeHistoryService.getTradeHistory(username);
        return ResponseEntity.ok(tradeHistoryDtos);
    }
    @PostMapping
    public void buy(@RequestBody OrderDto orderDto) {
        tradeHistoryService.order(orderDto, Orders.BUY);
    }
    @DeleteMapping
    public void sell(@RequestBody OrderDto orderDto) {
        tradeHistoryService.order(orderDto, Orders.SELL);
    }

    @DeleteMapping("/cancel")
    public void cancel(@RequestBody OrderDto orderDto) {

    }
}
