package com.ourMenu.backend.domain.article.api.request;

import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArticleMenuRequest {

    private String placeTitle;
    private String menuTitle;
    private int menuPrice;
    private String menuImgUrl;
    private String menuAddress;

    private String menuMemoTitle;
    private String menuIconType;
    private String placeMemo;
    private double placeLatitude;
    private double placeLongitude;


    public static ArticleMenu toEntity(ArticleMenuRequest articleMenuRequest){
        return ArticleMenu.builder()
                .placeTitle(articleMenuRequest.getPlaceTitle())
                .title(articleMenuRequest.getMenuTitle())
                .price(articleMenuRequest.getMenuPrice())
                .imgUrl(articleMenuRequest.getMenuImgUrl())
                .address(articleMenuRequest.getMenuAddress())
                .menuMemoTitle(articleMenuRequest.getMenuMemoTitle())
                .menuIconType(articleMenuRequest.getMenuIconType())
                .placeMemo(articleMenuRequest.getPlaceMemo())
                .placeLatitude(articleMenuRequest.getPlaceLatitude())
                .placeLongitude(articleMenuRequest.getPlaceLongitude())
                .build();
    }

}
