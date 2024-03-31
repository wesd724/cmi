package com.jkb.cmi.repository;

import com.jkb.cmi.dto.APIResponseDto;
import com.jkb.cmi.entity.type.Orders;

import java.util.List;

public interface TradeHistoryRepositoryCustom {

    void tradeCompleteProcessing(List<APIResponseDto> dtoList, Orders orders);
}
