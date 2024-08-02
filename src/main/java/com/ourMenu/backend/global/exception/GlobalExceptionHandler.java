package com.ourMenu.backend.global.exception;

import com.ourMenu.backend.global.util.ApiUtils;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return handleException(e,ErrorCode.INTERNAL_SERVER_ERROR, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<?> handleValidationException(ValidationException e) {
        String message = e.getMessage() != null ? e.getMessage() : ErrorCode.VALIDATION_ERROR.getMessage();
        return handleException(e, ErrorCode.VALIDATION_ERROR, message);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> handleException(RuntimeException e) {
        String message = e.getMessage() != null ? e.getMessage() : ErrorCode.INTERNAL_SERVER_ERROR.getMessage();
        return handleException(e, ErrorCode.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(JwtException.class)
    private ResponseEntity<?> handleJwtException(JwtException e) {
        String message = e.getMessage() != null ? e.getMessage() : ErrorCode.JWT_TOKEN_ERROR.getMessage();
        return handleException(e, ErrorCode.JWT_TOKEN_ERROR, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        return handleException(e,ErrorCode.MISSING_PARAMETER,ErrorCode.MISSING_PARAMETER.getMessage());
    }
  
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleBusinessException(CustomException e) {
        return handleException(e, e.getErrorCode(), e.getMessage());
    }
  
    private ResponseEntity<?> handleException(Exception e, ErrorCode errorCode, String message) {
        log.error("{}: {}", errorCode, e.getMessage());
        return ApiUtils.error(ErrorResponse.of(errorCode, message));
    }

}
