package com.jkb.cmi.notification.controller;

import com.jkb.cmi.notification.service.NotificationService;
import com.jkb.cmi.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final SseService sseService;
    private final NotificationService notificationService;

    @PostMapping("/all")
    public void sendAll() {
        sseService.sendNotificationToAll();
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
