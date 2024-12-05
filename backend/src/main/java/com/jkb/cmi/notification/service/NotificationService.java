package com.jkb.cmi.notification.service;

import com.jkb.cmi.notification.dto.NotificationDto;
import com.jkb.cmi.notification.entity.Notification;
import com.jkb.cmi.tradehistory.entity.TradeHistory;
import com.jkb.cmi.user.entity.User;
import com.jkb.cmi.event.NotificationEvent;
import com.jkb.cmi.event.TradeHistoryEvent;
import com.jkb.cmi.notification.repository.NotificationRepository;
import com.jkb.cmi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(propagation =  Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveNotification(TradeHistoryEvent event) {
        TradeHistory tradeHistory = event.getTradeHistory();
        Notification notification = Notification.builder()
                .user(tradeHistory.getUser())
                .tradeHistory(tradeHistory)
                .build();

        notificationRepository.save(notification); // this tradeHistory fk => s-lock
        eventPublisher.publishEvent(new NotificationEvent(tradeHistory.getUser().getUsername()));
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> findNotificationByUsername(String username) {
        List<NotificationDto> notificationRespons = notificationRepository.findNotificationByUsername(username);
        return notificationRespons;
    }

    public void checkNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public void checkAllNotification(String username) {
        User user = userRepository.getByUsername(username);
        notificationRepository.checkAllNotification(user.getId());
    }
}
