package com.ourMenu.backend.domain.article.exception;

import com.ourMenu.backend.global.exception.CustomException;
import com.ourMenu.backend.global.exception.ErrorCode;

public class NoSuchArticleException extends CustomException {
    public NoSuchArticleException(String message){
        super(message,ErrorCode.NO_SUCH_ELEMENT);
    }
}
