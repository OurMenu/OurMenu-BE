package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Slf4j
public class MenuDetailDto {
    private Long groupId;
    private String menuTitle;
    private int menuPrice;
    private String menuMemoTitle;
    private String menuMemo;
    private String menuIconType;
    private PlaceInfoDTO menuPlaceInfo;
    private List<TagDTO> menuTags;
    private List<MenuImageDto> menuImages; // 이미지 리스트
    private List<PlaceMenuFolderDTO> menuFolders; // 메뉴 폴더 리스트

    // DTO 변환 메서드
    public static MenuDetailDto toDto(List<Menu> menus) {

        List<MenuList> menuLists = new ArrayList<>();

        for (Menu menu : menus) {
            menuLists.add(menu.getMenuList());
        }

        List<PlaceMenuFolderDTO> folderDTOs = menuLists.stream() // 메뉴의 폴더 리스트
                .map(menuList -> new PlaceMenuFolderDTO(menuList.getTitle(), menuList.getIconType(), menuList.getId())) // 폴더 DTO 변환
                .collect(Collectors.toList());


        return fromMenu(menus.get(0), folderDTOs);



    }

    private static MenuDetailDto fromMenu(Menu menu, List<PlaceMenuFolderDTO> menuFolders) {
        log.info("e" + menu.getMemoTitle());
        List<TagDTO> tagDTOs = menu.getTags().stream() // MenuTag 리스트로부터 스트림 생성
                .map(menuTag -> TagDTO.fromTag(menuTag)) // MenuTag를 TagDTO로 변환
                .collect(Collectors.toList());

        List<MenuImageDto> imageDTOs = menu.getImages().stream()
                .map(img -> new MenuImageDto(img.getUrl())) // 이미지 DTO 변환
                .collect(Collectors.toList());

        // Place 정보 생성
        PlaceInfoDTO placeInfoDTO = PlaceInfoDTO.toDto(menu.getPlace());


        return MenuDetailDto.builder()
                .groupId(menu.getGroupId()) // 메뉴 ID 추가
                .menuTitle(menu.getTitle())
                .menuPrice(menu.getPrice())
                .menuMemoTitle(menu.getMemoTitle())
                .menuMemo(menu.getMemo()) // 메모 추가
                .menuIconType(menu.getMenuIconType()) // 아이콘 추가
                .menuTags(tagDTOs) // 태그 리스트 추가
                .menuImages(imageDTOs) // 이미지 리스트 추가
                .menuFolders(menuFolders)
                .menuPlaceInfo(placeInfoDTO)
                .build();
    }
}


