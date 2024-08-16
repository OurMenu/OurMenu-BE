package com.ourMenu.backend.domain.menu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuPlaceDTO {
    private Long groupId;
    private Long placeId;
    private String menuTitle;
    private String menuIconType;
    private Double latitude;
    private Double longitude;
}
