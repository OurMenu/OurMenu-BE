package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.DefaultTag;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@Builder
public class GetTagRecommends {

    private String tagName;

    private List<GetRecommand> menus;


    public static GetTagRecommends toDto(List<MenuDto> menus, DefaultTag tag) {
        List<GetRecommand> recommandList = menus.stream().map(GetRecommand::toDto).toList();
        return GetTagRecommends.builder()
                .tagName(tag.getTagName())
                .menus(recommandList)
                .build();

    }
}
