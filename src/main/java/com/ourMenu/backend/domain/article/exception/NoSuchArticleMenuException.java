package com.ourMenu.backend.domain.article.exception;

import com.ourMenu.backend.global.exception.CustomException;
import com.ourMenu.backend.global.exception.ErrorCode;

public class NoSuchArticleMenuException extends CustomException {
    public NoSuchArticleMenuException(){
        super("해당하는 게시물메뉴가 없습니다",ErrorCode.NO_SUCH_ELEMENT);
    }
}
