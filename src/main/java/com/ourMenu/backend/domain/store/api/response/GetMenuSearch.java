package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.Menu;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetMenuSearch {

    private String name;
    private String price;

    public static GetMenuSearch toDto(Menu menu){
        return GetMenuSearch.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }
}
