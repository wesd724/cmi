package com.jkb.cmi.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponse {
    private String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
