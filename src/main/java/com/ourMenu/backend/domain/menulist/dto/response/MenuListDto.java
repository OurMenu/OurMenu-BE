package com.ourMenu.backend.domain.menulist.dto.response;

import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import com.ourMenu.backend.global.common.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MenuListDto {

    private Long id;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String title;

    private String imgUrl;

    private List<MenuDto> menus;

}
