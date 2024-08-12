package com.ourMenu.backend.domain.menu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaceMenuDTO {
//    private Long menuId;
    private Long groupId;
    private String menuTitle;
    private int menuPrice;
    private String menuIconType;
    private List<TagDTO> menuTags;
    private List<MenuImageDto> menuImgsUrl;
    private List<PlaceMenuFolderDTO> menuFolders;
    private int menuFolderCount;
}
