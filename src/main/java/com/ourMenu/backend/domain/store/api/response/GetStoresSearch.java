package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.Menu;
import com.ourMenu.backend.domain.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class GetStoresSearch {

    private String id;
    private String name;
    private String address;
    private String type;
    private List<String> images;
    private List<GetMenuSearch> menus;
    private String time;
    private String mapx;
    private String mapy;

    public static GetStoresSearch toDto(Store store){
        List<GetMenuSearch> menuList;
        if (store.getMenu() != null) {
            menuList = store.getMenu().stream()
                    .map(GetMenuSearch::toDto)
                    .toList();
        }
        else{
            menuList= Collections.emptyList();
        }
        return GetStoresSearch.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .type(store.getType())
                .images(store.getImages())
                .menus(menuList)
                .time(store.getTime())
                .mapx(formatCoordinate(store.getMapx()))
                .mapy(formatCoordinate(store.getMapy()))
                .build();
    }
    private static String formatCoordinate(String coordinate) {
        double value = Double.parseDouble(coordinate) / 10000000.0;
        return String.format("%.7f", value);

    }
}
