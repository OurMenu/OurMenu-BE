package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.onboarding.domain.DefaultTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class GetTagRecommends {

    private String tagName;

    private List<GetRecommend> menus;


    public static GetTagRecommends toDtoOwn(List<Menu> menuList, DefaultTag tag) {
        List<GetRecommend> recommandList = menuList.stream().map(menu->GetRecommend.toDto(menu,true)).toList();
        return GetTagRecommends.builder()
                .tagName(tag.getTagMemo())
                .menus(new ArrayList<>(recommandList))
                .build();

    }

    public static GetTagRecommends toDtoOther(List<Menu> menuList, DefaultTag tag) {
        List<GetRecommend> recommandList = menuList.stream().map(menu->GetRecommend.toDto(menu,false)).toList();
        return GetTagRecommends.builder()
                .tagName(tag.getTagMemo())
                .menus(new ArrayList<>(recommandList))
                .build();

    }

    public void addAll(List<Menu> menuList) {
        List<GetRecommend> recommandList = menuList.stream().map(menu->GetRecommend.toDto(menu,false)).toList();
        if (menus == null) {
            menus = new ArrayList<>();  // 가변 리스트로 초기화
        }
        menus.addAll(recommandList);
    }
}
