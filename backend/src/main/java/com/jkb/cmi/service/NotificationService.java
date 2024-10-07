package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.entity.Notification;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.repository.NotificationRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void saveNotification(TradeHistory tradeHistory) {
        Notification notification = Notification.builder()
                .user(tradeHistory.getUser())
                .tradeHistory(tradeHistory)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findNotificationByUsername(String username) {
        List<NotificationResponse> notificationResponses = notificationRepository.findNotificationByUsername(username);
        return notificationResponses;
    }

    public void checkNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public void checkAllNotification(String username) {
        User user = userRepository.getByUsername(username);
        notificationRepository.checkAllNotification(user.getId());
    }
}
