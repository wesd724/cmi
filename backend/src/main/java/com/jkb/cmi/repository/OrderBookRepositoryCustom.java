package com.jkb.cmi.repository;

import com.jkb.cmi.dto.OrderBookDto;
import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.type.Orders;

import java.util.List;

public interface OrderBookRepositoryCustom {
    List<OrderBook> findByPriceForUpdate(Long currencyId, Double price, Orders orders);
    List<OrderBookDto> getByCurrencyId(Long currencyId);
}
