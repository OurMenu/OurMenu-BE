package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuIdDto {
    private Long menuId;

    public static MenuIdDto toDto(Menu menu) {
        return new MenuIdDto(menu.getId());
    }
}
