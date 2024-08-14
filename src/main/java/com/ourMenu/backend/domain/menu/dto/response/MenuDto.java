package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MenuDto {
    private Long groupId;
    private Long menuId;
    private String menuTitle;        // menuId 필드 제거
    private String placeTitle;
    private String placeAddress;
    private int menuPrice;
    private String menuImgUrl;



    public static List<MenuDto> toDto(List<Menu> menus) {
        List<MenuDto> dtoList = new ArrayList<>();
        Set<String> uniqueKeys = new HashSet<>();

        for (Menu menu : menus) {
            MenuDto dto = fromMenu(menu);
            String uniqueKey = dto.getMenuTitle() + "|" + dto.getPlaceTitle() + "|" +
                    dto.getPlaceAddress() + "|" + dto.getMenuPrice() + "|" +
                    dto.getGroupId();

            if (!uniqueKeys.contains(uniqueKey)) {
                uniqueKeys.add(uniqueKey);
                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    public static MenuDto toDto(Menu menu) {
        return fromMenu(menu);
    }

    private static MenuDto fromMenu(Menu menu) {
        return MenuDto.builder()
                .menuTitle(menu.getTitle())
                .menuId(menu.getId())
                .placeTitle(menu.getPlace().getTitle())
                .placeAddress(menu.getPlace().getAddress())
                .menuPrice(menu.getPrice())
                .groupId(menu.getGroupId())
                .menuImgUrl(menu.getImages().isEmpty() ? null : menu.getImages().get(0).getUrl())
                .build();
    }
}