package com.ourMenu.backend.domain.menu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuSearchDTO {
    private Long groupId;
    private String menuTitle;
    private String placeTitle;
    private String placeAddress;
}
