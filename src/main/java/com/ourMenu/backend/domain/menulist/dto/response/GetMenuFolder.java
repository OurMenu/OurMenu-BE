package com.ourMenu.backend.domain.menulist.dto.response;

import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetMenuFolder {
    private Long menuFolderId;
    private String menuFolderTitle;
    private String menuFolderIcon;
    private String menuFolderImg;
    private int menuCount;
    private List<MenuDto> menus;
}
