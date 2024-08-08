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

    public static List<MenuDto> toDto(List<Menu> menus){
        return menus.stream()
                .map(menu -> MenuDto.builder()
                        .menuId(menu.getId())
                        .menuTitle(menu.getTitle())
                        .placeTitle(menu.getPlace().getTitle())
                        .placeAddress(menu.getPlace().getAddress())
                        .menuPrice(menu.getPrice())
                        .menuImgUrl(menu.getImages().isEmpty() ? null : menu.getImages().get(0).getUrl())
                        .build())
                .collect(Collectors.toList());
    }


}
