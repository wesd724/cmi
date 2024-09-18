package com.jkb.cmi.repository;

import com.jkb.cmi.entity.OrderBook;
import com.jkb.cmi.entity.type.Orders;

import java.util.List;

public interface OrderBookRepositoryCustom {
    List<OrderBook> findByPriceForUpdate(Long currencyId, Double price, Orders orders);
}
