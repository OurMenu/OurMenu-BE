package com.ourMenu.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"G500","서버 내부에서 에러가 발생하였습니다"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "G400", "요청 파라미터가 누락되었습니다"),
    NO_SUCH_ELEMENT(HttpStatus.NOT_FOUND,"A404","해당하는 요소가 없습니다"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "G400", "올바르지 않은 요청입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "G401", "유효하지 않은 인증입니다"),

    // jwt
    JWT_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "G4001", "유효하지 않은 토큰입니다"),

    // user
    EMAIL_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "G5001", "유효하지 않은 이메일입니다"),
    DUPLICATED_EMAIL_ERROR(HttpStatus.BAD_REQUEST, "G5002", "이미 존재하는 이메일입니다"),
    INVALID_PASSWORD_ERROR(HttpStatus.UNAUTHORIZED, "G5003", "올바르지 않은 비밀번호입니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "G5004", "유저가 존재하지 않습니다."),

    //search
    SEARCH_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND,"S404","검색 결과가 없습니다"),

    //menu, menuList
    MENU_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "M401", "메뉴판이 존재하지 않습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "M402", "메뉴가 존재하지 않습니다"),
    PRIORITY_NOT_VALID(HttpStatus.BAD_REQUEST, "M403", "유효하지 않은 우선순위입니다."),

    //S3 image
    IMAGE_NOT_LOADED_ERROR(HttpStatus.BAD_REQUEST, "I400", "이미지 업로드에 실패하였습니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
