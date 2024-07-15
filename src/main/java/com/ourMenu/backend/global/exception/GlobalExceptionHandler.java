package com.ourMenu.backend.global.exception;

import com.ourMenu.backend.global.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> handleException(Exception e) {
        return handleException(e, ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    private ResponseEntity<?> handleException(Exception e, ErrorCode errorCode, String message) {
        return ApiUtils.error(ErrorResponse.of(errorCode, message));
    }
}
