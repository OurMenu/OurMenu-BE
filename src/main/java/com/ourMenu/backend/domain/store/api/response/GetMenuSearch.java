package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.Menu;
import lombok.Builder;
import lombok.Getter;

import java.text.NumberFormat;
import java.util.Locale;

@Builder
@Getter
public class GetMenuSearch {

    private String menuTitle;
    private String menuPrice;

    private static String formatPrice(String price) {
        int priceInt = Integer.parseInt(price);
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(priceInt);
    }

    public static GetMenuSearch toDto(Menu menu){
        return GetMenuSearch.builder()
                .menuTitle(menu.getName())
                .menuPrice(formatPrice(menu.getPrice()))
                .build();
    }
}
