package com.ourMenu.backend.domain.menu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuSearchDTO {
    private Long menuId;
    private Long menuTitle;
    private Long placeTitle;
    private String placeAddress;
}
