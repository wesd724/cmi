package com.jkb.cmi.controller;

import com.jkb.cmi.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {
    private final SseService sseService;

    @GetMapping(value = "/connection", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Accel-Buffering", "no");
        return ResponseEntity.ok().headers(headers).body(sseService.subscribe(username));
    }

    @DeleteMapping("/close")
    public ResponseEntity<Void> close(String username) {
        sseService.close(username);
        return ResponseEntity.ok().build();
    }
}
