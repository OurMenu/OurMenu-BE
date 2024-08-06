package com.ourMenu.backend.domain.menu.exception;

public class MenuNotFoundException extends RuntimeException{

    public MenuNotFoundException(){
        this("메뉴를 찾을 수 없습니다.");
    }

    public MenuNotFoundException(String msg){
        super(msg);
    }
}
