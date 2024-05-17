package com.jkb.cmi.common.exception;

import lombok.NoArgsConstructor;

public class DefaultException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "오류 발생";

    public DefaultException() {
        super(DEFAULT_MESSAGE);
    }
    public DefaultException(final String message) {
        super(message);
    }
}
