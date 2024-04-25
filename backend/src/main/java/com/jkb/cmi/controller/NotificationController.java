package com.jkb.cmi.controller;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

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

    @GetMapping("/test")
    public void test() {
        List<NotificationResponse> notificationResponses = notificationService.findNotificationByUsername("test");
        notificationService.sendAll(notificationResponses);
    }
}
