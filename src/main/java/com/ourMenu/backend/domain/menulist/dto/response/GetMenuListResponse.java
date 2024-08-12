package com.ourMenu.backend.domain.menulist.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetMenuListResponse {
    private Long menuFolderId;
    private String menuFolderTitle;
    private Long menuCount;
    private String menuFolderImgUrl;
    private String menuFolderIcon;
    private Long menuFolderPriority;
    private List<MenuGroupIdDTO> menuIds;
}
