package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.TradeHistoryResponse;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeHistoryService {
    private final TradeHistoryRepository tradeHistoryRepository;

    @Transactional(readOnly = true)
    public List<TradeHistoryResponse> getTradeHistory(String username) {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUsername(username);
        return TradeHistoryResponse.tolist(tradeHistories);
    }
}
