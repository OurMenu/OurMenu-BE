package com.ourMenu.backend.domain.menulist.dto.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MenuListResponseDTO {
    private Long menuFolderId;
    private String menuFolderTitle;
    private String menuFolderImgUrl;
    private String menuFolderIcon;
    private Long menuFolderPriority;
    private List<Menu> menuList;
}
