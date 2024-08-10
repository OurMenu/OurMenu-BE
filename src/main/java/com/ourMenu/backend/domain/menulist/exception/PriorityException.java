package com.ourMenu.backend.domain.menulist.exception;

public class PriorityException extends RuntimeException {

    public PriorityException(){
        this("유효하지 않은 우선순위입니다.");
    }
    public PriorityException(String s) {
        super(s);
    }
}
