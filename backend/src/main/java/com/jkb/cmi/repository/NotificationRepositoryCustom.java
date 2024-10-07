package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.entity.TradeHistory;

import java.util.List;

public interface NotificationRepositoryCustom {

    void saveAllNotification(List<TradeHistory> tradeHistories);
    List<NotificationResponse> findNotificationByUsername(String username);
}
