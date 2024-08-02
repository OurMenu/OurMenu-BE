package com.ourMenu.backend.domain.menu.dto.request;

import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMenuRequest {

    private String title;
    private int price;
    private String memo;
    private String icon;
    private String menuListTitle;

    // 식당 관련 정보
    private StoreRequestDTO storeInfo;

    // 태그 관련 정보
    private List<TagRequestDto> tagInfo;

}
