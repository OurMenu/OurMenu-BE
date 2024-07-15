package com.ourMenu.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorCode {
    private final HttpStatus status;
    private final String code;
    private final String message;
}
