package com.jkb.cmi.orderbook.repository;

import com.jkb.cmi.orderbook.dto.OrderBookDto;
import com.jkb.cmi.orderbook.entity.OrderBook;
import com.jkb.cmi.common.enums.Orders;

import java.util.List;

public interface OrderBookRepositoryCustom {
    List<OrderBook> findByPriceForUpdate(Long currencyId, Double price, Orders orders);
    List<OrderBookDto> getByCurrencyId(Long currencyId);
}
