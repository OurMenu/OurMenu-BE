package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.MenuImage;
import com.ourMenu.backend.domain.menu.domain.Tag;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaceMenuDTO {
    private Long menuId;
    private String menuTitle;
    private int price;
    private String icon;
    private List<TagDTO> tags;
    private List<MenuImage> images;
    private PlaceMenuFolderDTO menuFolder;
}
