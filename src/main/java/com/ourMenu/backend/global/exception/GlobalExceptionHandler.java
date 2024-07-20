package com.ourMenu.backend.global.exception;

import com.ourMenu.backend.global.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> handleException(Exception e) {
        return handleException(e, ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return handleException(e,ErrorCode.INTERNAL_SERVER_ERROR, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    private ResponseEntity<?> handleException(Exception e, ErrorCode errorCode, String message) {
        return ApiUtils.error(ErrorResponse.of(errorCode, message));
    }
}
