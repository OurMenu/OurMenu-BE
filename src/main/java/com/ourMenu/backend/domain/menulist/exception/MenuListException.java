package com.ourMenu.backend.domain.menulist.exception;

import com.ourMenu.backend.global.exception.ErrorCode;

public class MenuListException extends RuntimeException{

    public MenuListException() {
        this("메뉴판이 존재하지 않습니다.");
    }

    public MenuListException(String msg) {
        super(msg);
    }
}
