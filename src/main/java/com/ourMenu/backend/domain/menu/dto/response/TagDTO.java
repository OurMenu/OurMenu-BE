package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.MenuTag;
import com.ourMenu.backend.domain.menu.domain.Tag;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TagDTO {
    private String tagTitle;
    private boolean isCustom;

    public static TagDTO fromTag(MenuTag menuTag) {
        return TagDTO.builder()
                .tagTitle(menuTag.getTag().getName()) // MenuTag를 통해 Tag 객체의 title 가져오기
                .isCustom(menuTag.getTag().getIsCustom()) // MenuTag의 isCustom 값 사용
                .build();
    }
}
