package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetRecommend {

    private String menuImgUrl;
    private String menuTitle;
    private String placeName;
    private Long groupId;
    private boolean isUserOwned;
    public static GetRecommend toDto(Menu menu, boolean isUserOwned){
        return GetRecommend.builder()
                .menuImgUrl(menu.getImages() != null && !menu.getImages().isEmpty() ? menu.getImages().get(0).getUrl() : null)
                .menuTitle(menu.getTitle())
                .placeName(menu.getPlace().getTitle())
                .groupId(menu.getGroupId())
                .isUserOwned(isUserOwned)
                .build();
    }

    public static GetRecommend toDto(MenuDto menuDto){
        return GetRecommend.builder()
                .menuImgUrl(menuDto.getMenuImgUrl())
                .menuTitle(menuDto.getMenuTitle())
                .placeName(menuDto.getPlaceAddress())
                .groupId(menuDto.getGroupId())
                .build();
    }

}
