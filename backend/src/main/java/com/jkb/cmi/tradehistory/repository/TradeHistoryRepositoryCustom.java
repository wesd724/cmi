package com.jkb.cmi.tradehistory.repository;

import com.jkb.cmi.external.dto.TickerAPIResponse;
import com.jkb.cmi.common.enums.Orders;

import java.util.List;

public interface TradeHistoryRepositoryCustom {

    void tradeCompleteProcessing(List<TickerAPIResponse> dtoList, Orders orders);
}
