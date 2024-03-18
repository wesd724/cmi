package com.jkb.cmi.service;

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
    public List<TradeHistoryDto> getTradeHistory(Long userId) {
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUser_id(userId);
        return TradeHistoryDto.tolist(tradeHistories);
    }
    public void buyOrder(OrderDto orderDto) {
        User user = userRepository.getReferenceById(orderDto.getUserId());
        Currency currency = currencyRepository.getReferenceById(orderDto.getCurrencyId());

        long tradePrice = Math.round(orderDto.getAmount() * orderDto.getPrice());

        TradeHistory tradeHistory = TradeHistory.builder()
                .user(user).currency(currency).orders(Orders.BUY)
                .amount(orderDto.getAmount()).price(orderDto.getPrice())
                .complete(false).build();

        tradeHistoryRepository.save(tradeHistory);
    }

    public void sellOrder(OrderDto orderDto) {
        User user = userRepository.getReferenceById(orderDto.getUserId());
        Currency currency = currencyRepository.getReferenceById(orderDto.getCurrencyId());

        long tradePrice = Math.round(orderDto.getAmount() * orderDto.getPrice());

        TradeHistory tradeHistory = TradeHistory.builder()
                .user(user).currency(currency).orders(Orders.SELL)
                .amount(orderDto.getAmount()).price(orderDto.getPrice())
                .complete(false).build();

        tradeHistoryRepository.save(tradeHistory);
    }
}
