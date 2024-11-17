package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.event.NotificationEvent;
import com.jkb.cmi.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SseService {
    private static final Long timeout = 12L * 1000 * 60 * 60;

    private final SseRepository sseRepository;
    private final NotificationService notificationService;

    public SseEmitter subscribe(String username) {
        SseEmitter emitter = sseRepository.save(username, new SseEmitter(timeout));

        emitter.onCompletion(() -> {
            log.info("completion");
            sseRepository.deleteByUsername(emitter, username);
        });
        emitter.onTimeout(() -> {
            log.info("timeout");
            sseRepository.deleteByUsername(emitter, username);
        });
        emitter.onError((e) -> {
            log.info("error");
            sseRepository.deleteByUsername(emitter, username);
        });

        sendEvent(emitter, "connect", "connection complete.");
        return emitter;
    }

    public void close(String username) {
        List<SseEmitter> emitters = sseRepository.findByUsername(username);

        emitters.forEach(emitter -> sendEvent(emitter, "connect", ""));
    }

    private void sendEvent(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data)
            );
        } catch (IOException e) {
            emitter.complete();
        }
    }

    public void sendNotificationToAll() {
        Map<String, List<SseEmitter>> emitters = sseRepository.findAll();

        emitters.forEach((username, emitterList) -> {
                    List<NotificationResponse> userData = notificationService.findNotificationByUsername(username);
                    //System.out.println(username + ": " + emitterList.size());
                    emitterList.forEach(emitter -> {
                        sendEvent(emitter, "message", userData);
                    });
                }
        );
    }

    @Async("asyncEvent")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationToUser(NotificationEvent event) {
        List<SseEmitter> emitters = sseRepository.findByUsername(event.getUsername());
        if (emitters != null) {
            List<NotificationResponse> userData = notificationService.findNotificationByUsername(event.getUsername());
            emitters.forEach(emitter -> {
                sendEvent(emitter, "message", userData);
            });
        }
    }

    public void sendEventToAll(String eventName, Object data) {
        Map<String, List<SseEmitter>> emitters = sseRepository.findAll();

        emitters.forEach((username, emitterList) -> {
                    //System.out.println(username + ": " + emitterList.size());
                    emitterList.forEach(emitter -> {
                        sendEvent(emitter, eventName, data);
                    });
                }
        );
    }

    public void sendByUsername(String username, Object data) {
        List<SseEmitter> emitters = sseRepository.findByUsername(username);

        emitters.forEach(emitter -> {
            sendEvent(emitter, "message", data);
        });
    }
}
