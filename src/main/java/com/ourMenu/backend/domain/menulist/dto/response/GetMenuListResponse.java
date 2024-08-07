package com.ourMenu.backend.domain.menulist.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMenuListResponse {
    private String menuFolderTitle;
    private Long menuCount;
    private String menuFolderImgUrl;
    private String menuFolderIcon;
    private Long menuFolderPriority;
}
