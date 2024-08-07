package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.MenuImage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaceMenuDTO {
    private Long menuId;
    private String menuTitle;
    private int menuPrice;
    private String menuIcon;
    private List<TagDTO> menuTags;
    private List<MenuImageDto> menuImgsUrl;
    private PlaceMenuFolderDTO menuFolder;
}
