package com.ourMenu.backend.domain.menulist.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuListResponseDTO {
    private Long menuFolderId;
    private String menuFolderTitle;
    private String menuFolderImgUrl;
    private String menuFolderIcon;
    private Long menuFolderPriority;
}
