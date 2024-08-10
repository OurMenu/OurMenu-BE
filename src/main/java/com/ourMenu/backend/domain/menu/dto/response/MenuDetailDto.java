package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuImage;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class MenuDetailDto {
    private Long groupId;
    private String menuTitle;
    private int price;
    private String memo;
    private String icon;
    private List<TagDTO> tags;
    private List<MenuImageDto> images; // 이미지 리스트
    private List<PlaceMenuFolderDTO> menuFolder; // 메뉴 폴더 리스트

    // DTO 변환 메서드
    public static MenuDetailDto toDto(List<Menu> menus) {

        List<MenuList> menuLists = new ArrayList<>();

        for (Menu menu : menus) {
            menuLists.add(menu.getMenuList());
        }

        List<PlaceMenuFolderDTO> folderDTOs = menuLists.stream() // 메뉴의 폴더 리스트
                .map(menuList -> new PlaceMenuFolderDTO(menuList.getTitle(), menuList.getIconType())) // 폴더 DTO 변환
                .distinct() // 중복 제거
                .collect(Collectors.toList());


        MenuDetailDto menuDetailDto = fromMenu(menus.get(0), folderDTOs);


        return menuDetailDto;
    }



    private static MenuDetailDto fromMenu(Menu menu, List<PlaceMenuFolderDTO> menuFolders) {
        List<TagDTO> tagDTOs = menu.getTags().stream() // MenuTag 리스트로부터 스트림 생성
                .map(menuTag -> TagDTO.fromTag(menuTag)) // MenuTag를 TagDTO로 변환
                .collect(Collectors.toList());

        List<MenuImageDto> imageDTOs = menu.getImages().stream()
                .map(img -> new MenuImageDto(img.getUrl())) // 이미지 DTO 변환
                .collect(Collectors.toList());

        return MenuDetailDto.builder()
                .groupId(menu.getGroupId()) // 메뉴 ID 추가
                .menuTitle(menu.getTitle())
                .price(menu.getPrice())
                .memo(menu.getMemo()) // 메모 추가
                .icon(menu.getIcon()) // 아이콘 추가
                .tags(tagDTOs) // 태그 리스트 추가
                .images(imageDTOs) // 이미지 리스트 추가
                .menuFolder(menuFolders)
                .build();
    }
}


