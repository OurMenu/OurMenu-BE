package com.ourMenu.backend.domain.menu.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TagDTO {
    private String tagTitle;
    private boolean isCustom;
}
