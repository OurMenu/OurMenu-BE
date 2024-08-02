package com.ourMenu.backend.domain.user.exception;

public class EmailDuplicationException extends RuntimeException {

    public EmailDuplicationException() {
        this("중복된 이메일입니다");
    }

    public EmailDuplicationException(String msg) {
        super(msg);
    }

}
