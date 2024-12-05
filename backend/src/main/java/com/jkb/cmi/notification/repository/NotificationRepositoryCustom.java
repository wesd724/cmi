package com.jkb.cmi.notification.repository;

import com.jkb.cmi.notification.dto.NotificationDto;
import com.jkb.cmi.tradehistory.entity.TradeHistory;

import java.util.List;

public interface NotificationRepositoryCustom {

    void saveAllNotification(List<TradeHistory> tradeHistories);
    List<NotificationDto> findNotificationByUsername(String username);
}
