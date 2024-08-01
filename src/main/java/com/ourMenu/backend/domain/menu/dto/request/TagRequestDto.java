package com.ourMenu.backend.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDto {
    private String tagTitle;
    private Boolean isCustom;
}
