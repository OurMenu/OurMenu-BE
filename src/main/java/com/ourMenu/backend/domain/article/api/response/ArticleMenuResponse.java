package com.ourMenu.backend.domain.article.api.response;

import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArticleMenuResponse {

    private String menuTitle;
    private int menuPrice;
    private String menuImgUrl;
    private String menuAddress;

    public static ArticleMenuResponse toDto(ArticleMenu articleMenu){
        return ArticleMenuResponse.builder()
                .menuTitle(articleMenu.getTitle())
                .menuPrice(articleMenu.getPrice())
                .menuImgUrl(articleMenu.getMenuImage().getUrl())
                .menuAddress(articleMenu.getAddress())
                .build();
    }
}
