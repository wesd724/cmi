package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.entity.Notification;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.repository.NotificationRepository;
import com.jkb.cmi.repository.SseRepository;
import com.jkb.cmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SseService {
    private static final Long timeout = 30L * 1000 * 60;

    private final SseRepository sseRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

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

    public void close(String username) {
        List<SseEmitter> emitters = sseRepository.findByUsername(username);

        emitters.forEach(emitter -> emitter.complete());
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

    public void sendNotificationToAll() {
        Map<String, List<SseEmitter>> emitters = sseRepository.findAll();

        emitters.forEach((username, emitterList) -> {
            List<NotificationResponse> userData = findNotificationByUsername(username);
                    System.out.println(username + ": " + emitterList.size());
                    emitterList.forEach(emitter -> {
                        sendEvent(emitter, username, "message", userData);
                    });
                }
        );
    }

    public void sendEventToAll(String eventName, Object data) {
        Map<String, List<SseEmitter>> emitters = sseRepository.findAll();

        emitters.forEach((username, emitterList) -> {
                    System.out.println(username + ": " + emitterList.size());
                    emitterList.forEach(emitter -> {
                        sendEvent(emitter, username, eventName, data);
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
                .tradeHistory(tradeHistory)
                .build();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findNotificationByUsername(String username) {
        List<Notification> notifications = notificationRepository.findNotificationByUsername(username);
        return NotificationResponse.tolist(notifications);
    }

    public void checkNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public void checkAllNotification(String username) {
        User user = userRepository.getByUsername(username);
        notificationRepository.checkAllNotification(user.getId());
    }
}
