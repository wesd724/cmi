package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.APIResponse;
import com.jkb.cmi.entity.type.Orders;

import java.util.List;

public interface TradeHistoryRepositoryCustom {

    void tradeCompleteProcessing(List<APIResponse> dtoList, Orders orders);
}
