package com.jkb.cmi.service;

import com.jkb.cmi.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {
    private static final Long timeout = 30L * 1000 * 60;
    private final SseRepository sseRepository;

    public SseEmitter subscribe(String username) {
        SseEmitter emitter = sseRepository.save(username, new SseEmitter(timeout));

        emitter.onCompletion(() -> {
            log.info("completion");
            sseRepository.delete(username);
        });
        emitter.onTimeout(() -> {
            log.info("timeout");
            sseRepository.delete(username);
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
            sseRepository.delete(username);
        }
    }

    public void send(Object data) {
        System.out.println(data);
//        emitters.stream().forEach(emitter ->
//                sendEvent(emitter, "msg", data)
//        );
    }


}
