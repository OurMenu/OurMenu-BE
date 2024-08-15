package com.ourMenu.backend.domain.menulist.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetMenuFolderDTO {
    private int menuCount;
    private List<GetMenuFolderResponse> menuFolders;
}
