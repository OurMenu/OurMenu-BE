package com.ourMenu.backend.domain.menu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MapMenuDTO {
//    private Long menuId;
    private Long groupId;
    private String menuTitle;
    private int menuPrice;
    private String menuIconType;
    private String placeTitle;
    private Double latitude;
    private Double longitude;
    private List<TagDTO> menuTags;
    private List<MenuImageDto> menuImgsUrl;
    private MapMenuFolderDTO menuFolder;
}
