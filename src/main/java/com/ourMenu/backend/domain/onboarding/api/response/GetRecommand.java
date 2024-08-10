package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetRecommand {

    private String menuImgUrl;
    private String menuTitle;
    private String placeName;
    private Long groupId;
    public static GetRecommand toDto(Menu menu){
        return GetRecommand.builder()
                .menuImgUrl(menu.getImages() != null && !menu.getImages().isEmpty() ? menu.getImages().get(0).getUrl() : null)
                .menuImgUrl(menu.getTitle())
                .placeName(menu.getPlace().getAddress())
                .groupId(menu.getGroupId())
                .build();
    }

}
