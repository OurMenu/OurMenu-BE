package com.ourMenu.backend.domain.user.exception;

public class AuthEmailException extends RuntimeException {

    public AuthEmailException() {
        this("유효하지 않은 인증 코드입니다");
    }

    public AuthEmailException(String msg) {
        super(msg);
    }

}
