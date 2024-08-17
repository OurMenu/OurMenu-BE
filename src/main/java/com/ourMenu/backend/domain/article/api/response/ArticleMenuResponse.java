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

    private Long articleMenuId;
    private String placeTitle;
    private String menuTitle;
    private int menuPrice;
    private String menuImgUrl;
    private String menuAddress;
    private int sharedCount;

    private String menuMemoTitle;
    private String menuIconType;
    private String placeMemo;
    private double placeLatitude;
    private double placeLongitude;
    public static ArticleMenuResponse toDto(ArticleMenu articleMenu){
        return ArticleMenuResponse.builder()
                .articleMenuId(articleMenu.getId())
                .placeTitle(articleMenu.getPlaceTitle())
                .menuTitle(articleMenu.getTitle())
                .menuPrice(articleMenu.getPrice())
                .menuImgUrl(articleMenu.getImgUrl())
                .menuAddress(articleMenu.getAddress())
                .sharedCount(articleMenu.getSharedCount())
                .menuMemoTitle(articleMenu.getMenuMemoTitle())
                .menuIconType(articleMenu.getMenuIconType())
                .placeMemo(articleMenu.getPlaceMemo())
                .placeLatitude(articleMenu.getPlaceLatitude())
                .placeLongitude(articleMenu.getPlaceLongitude())
                .build();
    }
}
