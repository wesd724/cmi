package com.jkb.cmi.controller;

import com.jkb.cmi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/connection", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(String username) {
        return ResponseEntity.ok(notificationService.subscribe(username));
    }

    @DeleteMapping("/close")
    public ResponseEntity<Void> close(String username) {
        notificationService.close(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/all")
    public void sendAll() {
        notificationService.sendAll();
    }

    @DeleteMapping("/check")
    public ResponseEntity<Void> check(Long id) {
        notificationService.checkNotification(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/check-all")
    public ResponseEntity<Void> checkAll(String username) {
        notificationService.checkAllNotification(username);
        return ResponseEntity.ok().build();
    }


}
