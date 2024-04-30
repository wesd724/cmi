package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.APIResponse;
import com.jkb.cmi.dto.request.OrderRequest;
import com.jkb.cmi.dto.response.TradeHistoryResponse;
import com.jkb.cmi.entity.Currency;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.repository.CurrencyRepository;
import com.jkb.cmi.repository.TradeHistoryRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeHistoryService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final TradeHistoryRepository tradeHistoryRepository;

    @Transactional(readOnly = true)
    public List<TradeHistoryResponse> getTradeHistory(String username) {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUsername(username);
        return TradeHistoryResponse.tolist(tradeHistories);
    }

    public void order(OrderRequest orderRequest, Orders orders) {
        User user = userRepository.getByUsername(orderRequest.getUsername());
        Currency currency = currencyRepository.getReferenceById(orderRequest.getCurrencyId());

        TradeHistory tradeHistory = TradeHistory.builder()
                .user(user).currency(currency).orders(orders)
                .amount(orderRequest.getAmount()).price(orderRequest.getPrice())
                .complete(false).build();

        tradeHistoryRepository.save(tradeHistory);
    }

    public void completeProcess(List<APIResponse> apiResponses) {
        tradeHistoryRepository.tradeCompleteProcessing(apiResponses, Orders.BUY);
        tradeHistoryRepository.tradeCompleteProcessing(apiResponses, Orders.SELL);
    }

    public Boolean cancel(Long id) {
        TradeHistory tradeHistory = tradeHistoryRepository.getReferenceById(id);
        if(tradeHistory.isComplete())
            return false;
        tradeHistoryRepository.deleteById(id);
        return true;
    }
}
