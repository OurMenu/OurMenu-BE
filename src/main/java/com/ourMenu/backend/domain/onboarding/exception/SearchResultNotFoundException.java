package com.ourMenu.backend.domain.onboarding.exception;

import com.ourMenu.backend.global.exception.CustomException;
import com.ourMenu.backend.global.exception.ErrorCode;

public class SearchResultNotFoundException extends CustomException {

    public SearchResultNotFoundException(){
        super(ErrorCode.ON_BOARDING_STATE_NOT_FOUND);
    }

}