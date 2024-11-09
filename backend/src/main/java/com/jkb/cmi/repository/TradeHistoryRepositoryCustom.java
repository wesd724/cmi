package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.TickerAPIResponse;
import com.jkb.cmi.entity.type.Orders;

import java.util.List;

public interface TradeHistoryRepositoryCustom {

    void tradeCompleteProcessing(List<TickerAPIResponse> dtoList, Orders orders);
}
