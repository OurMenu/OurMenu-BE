package com.ourMenu.backend.domain.store.exception;

import com.ourMenu.backend.global.exception.CustomException;
import com.ourMenu.backend.global.exception.ErrorCode;

public class SearchResultNotFoundException extends CustomException {

    public SearchResultNotFoundException(){
        super(ErrorCode.SEARCH_RESULT_NOT_FOUND);
    }

}
