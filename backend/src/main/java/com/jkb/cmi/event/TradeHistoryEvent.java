package com.jkb.cmi.event;

import com.jkb.cmi.entity.TradeHistory;
import lombok.Getter;

@Getter
public class TradeHistoryEvent {
    private TradeHistory tradeHistory;

    public TradeHistoryEvent(TradeHistory tradeHistory) {
        this.tradeHistory = tradeHistory;
    }
}
