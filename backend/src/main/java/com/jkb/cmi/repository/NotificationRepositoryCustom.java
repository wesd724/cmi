package com.jkb.cmi.repository;

import com.jkb.cmi.entity.Notification;
import com.jkb.cmi.entity.TradeHistory;

import java.util.List;

public interface NotificationRepositoryCustom {

    void saveAllNotification(List<TradeHistory> tradeHistories);
    List<Notification> findNotificationByUsername(String username);
}
