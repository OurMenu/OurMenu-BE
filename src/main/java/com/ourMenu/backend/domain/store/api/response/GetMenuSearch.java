package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.Menu;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetMenuSearch {

    private String menuTitle;
    private String menuPrice;

    public static GetMenuSearch toDto(Menu menu){
        return GetMenuSearch.builder()
                .menuTitle(menu.getName())
                .menuPrice(menu.getPrice())
                .build();
    }
}
