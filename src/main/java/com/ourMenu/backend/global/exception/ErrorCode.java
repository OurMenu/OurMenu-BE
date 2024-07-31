package com.ourMenu.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"G500","서버 내부에서 에러가 발생하였습니다"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "G400", "요청 파라미터가 누락되었습니다"),

    //search
    SEARCH_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND,"S404","검색 결과가 없습니다")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
