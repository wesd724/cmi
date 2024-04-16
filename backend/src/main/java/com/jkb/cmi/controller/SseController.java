package com.jkb.cmi.controller;

import com.jkb.cmi.entity.User;
import com.jkb.cmi.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(String username) {
        return ResponseEntity.ok(sseService.subscribe(username));
    }

    @GetMapping("/test")
    public void test() {
        sseService.send(User.builder().username("123").password("asd").build());
    }
}
