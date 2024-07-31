package com.ourMenu.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"G500","서버 내부에서 에러가 발생하였습니다"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "G400", "올바르지 않은 요청입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "G401", "유효하지 않은 인증입니다"),
    JWT_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "G4001", "유효하지 않은 토큰입니다"),;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
