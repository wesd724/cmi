package com.jkb.cmi.orderbook.dto.request;

import com.jkb.cmi.currency.entity.Currency;
import com.jkb.cmi.orderbook.entity.OrderBook;
import com.jkb.cmi.tradehistory.entity.TradeHistory;
import com.jkb.cmi.user.entity.User;
import com.jkb.cmi.common.enums.Orders;
import com.jkb.cmi.tradehistory.entity.type.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class OrderRequest {
    private String username;
    private Long currencyId;
    private double amount;
    private double price;

    public TradeHistory toTradeHistoryEntity(User user, Currency currency, Orders orders) {
        return TradeHistory.builder()
                .user(user).currency(currency).orders(orders)
                .amount(amount).price(price)
                .status(Status.COMPLETE).build();
    }

    public OrderBook toEntity(User user, Currency currency, Orders orders) {
        return OrderBook.builder()
                .user(user).currency(currency).orders(orders)
                .originalAmount(amount).price(price).build();
    }
}
