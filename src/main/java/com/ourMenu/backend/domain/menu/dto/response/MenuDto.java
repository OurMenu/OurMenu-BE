package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MenuDto {
    private Long menuId;
    private String menuTitle;
    private String placeName;
    private String address;
    private int price;
    private String img;

    public static List<MenuDto> toDto(List<Menu> menus){
        return menus.stream()
                .map(menu -> MenuDto.builder()
                        .menuId(menu.getId())
                        .menuTitle(menu.getTitle())
                        .placeName(menu.getPlace().getTitle())
                        .address(menu.getPlace().getAddress())
                        .price(menu.getPrice())
                        .img(menu.getImages().isEmpty() ? null : menu.getImages().get(0).getUrl())
                        .build())
                .collect(Collectors.toList());
    }


}
