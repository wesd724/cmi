package com.jkb.cmi.tradehistory.controller;

import com.jkb.cmi.tradehistory.dto.TradeHistoryResponse;
import com.jkb.cmi.tradehistory.service.TradeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
