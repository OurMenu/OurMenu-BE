package com.ourMenu.backend.domain.menu.exception;

public class PlaceNotFoundException extends RuntimeException{

    public PlaceNotFoundException(){
        this("식당을 찾을 수 없습니다.");
    }

    public PlaceNotFoundException(String m){
        super(m);
    }
}
