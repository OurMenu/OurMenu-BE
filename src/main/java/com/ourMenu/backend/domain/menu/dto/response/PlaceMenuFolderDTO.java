package com.ourMenu.backend.domain.menu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceMenuFolderDTO {
    private String menuFolderTitle;
    private String menuIcon;
}
