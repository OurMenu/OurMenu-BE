package com.ourMenu.backend.domain.article.domain;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum ORDER_CRITERIA {

    //최신순
    CREATED_AT_DESC(Sort.Direction.DESC,"createdAt"),
    //오래된 순
    CREATED_AT_ASC(Sort.Direction.ASC,"createdAt"),
    //오래된 순
    // 조회수 많은 순
    VIEWS_DESC(Sort.Direction.DESC, "views"),

    // 조회수 적은 순
    VIEWS_ASC(Sort.Direction.ASC, "views");

    ;

    private final Sort.Direction direction;
    private final String property;

    ORDER_CRITERIA(Sort.Direction direction, String property) {
        this.direction = direction;
        this.property = property;
    }
}
