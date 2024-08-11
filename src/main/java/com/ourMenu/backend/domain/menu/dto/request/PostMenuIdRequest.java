package com.ourMenu.backend.domain.menu.dto.request;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMenuIdRequest {
    private Long menuFolderId;
}
