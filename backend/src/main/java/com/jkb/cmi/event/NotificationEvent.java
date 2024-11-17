package com.jkb.cmi.event;

import lombok.Getter;

@Getter
public class NotificationEvent {
    private String username;

    public NotificationEvent(String username) {
        this.username = username;
    }
}
