package com.ourMenu.backend.domain.menulist.exception;

public class ImageLoadException extends RuntimeException{
    public ImageLoadException(){
        this("이미지 업로드 중 오류가 발생했습니다.");
    }

    public ImageLoadException(String msg){
        super(msg);
    }
}
