package com.jkb.cmi.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseRepository {
    private Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String username, SseEmitter sseEmitter) {
        if(emitters.containsKey(username))
            return emitters.get(username);

        emitters.put(username, sseEmitter);
        return sseEmitter;
    }

    public void delete(String username) {
        emitters.remove(username);
    }
}
