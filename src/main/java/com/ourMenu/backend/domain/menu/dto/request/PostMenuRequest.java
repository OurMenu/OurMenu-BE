package com.ourMenu.backend.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMenuRequest {

    private String menuTitle;
    private int menuPrice;
    private String menuMemo;
    private String menuIcon;
    private String menuFolderTitle;

    // 식당 관련 정보
    private StoreRequestDTO storeInfo;

    // 태그 관련 정보
    private List<TagRequestDto> tagInfo;

}
