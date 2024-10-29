package com.jkb.cmi.event;

import lombok.Getter;

@Getter
public class UserSignUpEvent {
    private Long userId;

    public UserSignUpEvent(Long userId) {
        this.userId = userId;
    }
}
