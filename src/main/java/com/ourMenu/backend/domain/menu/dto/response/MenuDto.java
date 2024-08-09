package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MenuDto {
    private Long menuId;
    private String menuTitle;
    private String placeTitle;
    private String placeAddress;
    private int menuPrice;
    private String menuImgUrl;
    private Long groupId;

    public static List<MenuDto> toDto(List<Menu> menus) {
        return menus.stream()
                .map(MenuDto::fromMenu)
                .collect(Collectors.toList());
    }

    // 단일 Menu 객체를 받아 DTO로 변환하는 메서드
    public static MenuDto toDto(Menu menu) {
        return fromMenu(menu);
    }

    // Menu 객체를 DTO로 변환하는 내부 메서드
    private static MenuDto fromMenu(Menu menu) {
        return MenuDto.builder()
                .menuId(menu.getId())
                .menuTitle(menu.getTitle())
                .placeTitle(menu.getPlace().getTitle())
                .placeAddress(menu.getPlace().getAddress())
                .menuPrice(menu.getPrice())
                .groupId(menu.getGroupId())
                .menuImgUrl(menu.getImages().isEmpty() ? null : menu.getImages().get(0).getUrl())
                .build();
    }
}