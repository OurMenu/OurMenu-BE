package com.ourMenu.backend.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MapMenuFolderDTO {
    private String menuFolderTitle;
    private String menuFolderIcon;
    private int menuFolderCount;
}
