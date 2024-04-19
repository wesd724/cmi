package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.entity.Notification;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.repository.NotificationRepository;
import com.jkb.cmi.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private static final Long timeout = 30L * 1000 * 60;

    private final SseRepository sseRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String username) {
        SseEmitter emitter = sseRepository.save(username, new SseEmitter(timeout));

        emitter.onCompletion(() -> {
            log.info("completion");
            sseRepository.deleteByUsername(username);
        });
        emitter.onTimeout(() -> {
            log.info("timeout");
            sseRepository.deleteByUsername(username);
        });

        sendEvent(emitter, username, "connect", "connection complete.");
        return emitter;
    }

    private void sendEvent(SseEmitter emitter, String username, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data)
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            sseRepository.deleteByUsername(username);
        }
    }

    public void sendAll(Object data) {
        Map<String, List<SseEmitter>> emitters = sseRepository.findAll();

        emitters.forEach((username, emitterList) -> {
                    System.out.println(username + ": " + emitterList.size());
                    emitterList.forEach(emitter -> {
                        sendEvent(emitter, username, "message", data);
                    });
                }
        );
    }

    public void sendByUsername(String username, Object data) {
        List<SseEmitter> emitters = sseRepository.findByUsername(username);

        emitters.forEach(emitter -> {
            sendEvent(emitter, username, "message", data);
        });
    }

    public void saveNotification(TradeHistory tradeHistory) {
        Notification notification = Notification.builder()
                .user(tradeHistory.getUser())
                .currency(tradeHistory.getCurrency())
                .tradeHistory(tradeHistory)
                .isRead(false)
                .build();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findNotificationByUsername(String username) {
        List<Notification> notifications = notificationRepository.findNotificationByUsername(username);
        return NotificationResponse.tolist(notifications);
    }
}
