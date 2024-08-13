package com.ourMenu.backend.domain.article.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArticleMenuRequest {

    private String menuTitle;
    private int menuPrice;
    private String menuImgUrl;
    private String menuAddress;

}
