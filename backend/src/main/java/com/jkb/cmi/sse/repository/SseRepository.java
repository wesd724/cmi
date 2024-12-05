package com.jkb.cmi.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SseRepository {
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String username, SseEmitter sseEmitter) {
        List<SseEmitter> userEmitters = emitters.computeIfAbsent(username, k -> new CopyOnWriteArrayList<>());
        userEmitters.add(sseEmitter);
        return sseEmitter;
    }

    public void deleteByUsername(SseEmitter emitter, String username) {
        List<SseEmitter> userEmitters = emitters.get(username);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(username);
            }
        }
    }

    public List<SseEmitter> findByUsername(String username) {
        return emitters.get(username);
    }

    public Map<String, List<SseEmitter>> findAll() {
        return emitters;
    }


}
