package com.ourMenu.backend.domain.menulist.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuGroupIdDTO {
    private Long menuId;
    private Long groupId;
}
