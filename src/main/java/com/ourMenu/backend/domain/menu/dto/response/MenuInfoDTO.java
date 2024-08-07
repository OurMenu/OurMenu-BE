package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.MenuImage;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.LongFunction;

@Data
@Builder
public class MenuInfoDTO {
    private Long menuId;
    private Long placeId;
    private String menuTitle;
    private int price;
    private String memo;
    private String icon;
    private List<TagDTO> tags;
    private List<MenuImage> images;
    private PlaceMenuFolderDTO menuFolder;
}
