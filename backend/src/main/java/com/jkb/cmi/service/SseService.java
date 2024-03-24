package com.jkb.cmi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseService {
    private final Long timeout = 30L * 1000 * 60;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(timeout);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> {
            log.info("completion");
            this.emitters.remove(emitter);
            log.info("현재 커넥트 수: " + emitters.size());
        });
        emitter.onTimeout(() -> {
            log.info("timeout");
            this.emitters.remove(emitter);
        });
        System.out.println("연결 커넥트 수: " + emitters.size());
        sendEvent(emitter, "connect", "connection complete.");
        return emitter;
    }

    public void send(Object data) {
        System.out.println(emitters.size());
        emitters.stream().forEach(emitter ->
                sendEvent(emitter, "msg", data)
        );
    }

    private void sendEvent(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data)
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            this.emitters.remove(emitter);
        }
    }
}
