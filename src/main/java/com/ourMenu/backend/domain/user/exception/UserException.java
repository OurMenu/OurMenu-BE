package com.ourMenu.backend.domain.user.exception;

import com.ourMenu.backend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private ErrorCode errorCode;

    public UserException() {
        super("User exception is occurred");
    }

    public UserException(ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

}
