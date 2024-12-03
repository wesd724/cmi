package com.jkb.cmi.repository;

import com.jkb.cmi.dto.NotificationDto;
import com.jkb.cmi.entity.TradeHistory;

import java.util.List;

public interface NotificationRepositoryCustom {

    void saveAllNotification(List<TradeHistory> tradeHistories);
    List<NotificationDto> findNotificationByUsername(String username);
}
