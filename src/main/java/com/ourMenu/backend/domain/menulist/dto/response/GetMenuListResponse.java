package com.ourMenu.backend.domain.menulist.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMenuListResponse {
    private String title;
    private Long menuCount;
    private String imgUrl;
    private String iconType;
    private Long priority;
}
