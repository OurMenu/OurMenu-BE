package com.ourMenu.backend.domain.menulist.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuListResponseDTO {
    private Long id;
    private String title;
    private String imgUrl;
    private String iconType;
}
