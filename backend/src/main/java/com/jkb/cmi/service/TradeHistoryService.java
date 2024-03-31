package com.jkb.cmi.service;

import com.jkb.cmi.dto.APIResponseDto;
import com.jkb.cmi.dto.OrderDto;
import com.jkb.cmi.dto.TradeHistoryDto;
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
    public List<TradeHistoryDto> getTradeHistory(String username) {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUsername(username);
        return TradeHistoryDto.tolist(tradeHistories);
    }

    public void order(OrderDto orderDto, Orders orders) {
        User user = userRepository.getByUsername(orderDto.getUsername());
        Currency currency = currencyRepository.getReferenceById(orderDto.getCurrencyId());

        TradeHistory tradeHistory = TradeHistory.builder()
                .user(user).currency(currency).orders(orders)
                .amount(orderDto.getAmount()).price(orderDto.getPrice())
                .complete(false).build();

        tradeHistoryRepository.save(tradeHistory);
    }

    public void completeProcess(List<APIResponseDto> apiResponseDtos) {
        tradeHistoryRepository.tradeCompleteProcessing(apiResponseDtos, Orders.BUY);
        tradeHistoryRepository.tradeCompleteProcessing(apiResponseDtos, Orders.SELL);
    }
}
