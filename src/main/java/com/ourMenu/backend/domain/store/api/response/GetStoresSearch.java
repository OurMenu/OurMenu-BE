package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Builder
@Getter
public class GetStoresSearch {

    private String placeId;
    private String placeTitle;
    private String placeAddress;
    private String placeType;
    private List<String> placeImgsUrl;
    private List<GetMenuSearch> menus;
    private String timeInfo;
    private String latitude;
    private String longitude;

    public static String processTimeInfo(String timeInfo) {
        try {
            String[] lines = timeInfo.split("\n");

            StringBuilder processedTimeInfo = new StringBuilder();
            for (int i = 1; i < lines.length - 1; i++) {
                processedTimeInfo.append(lines[i]);
                if (i < lines.length - 2) {
                    processedTimeInfo.append("\n");
                }
            }

            return processedTimeInfo.toString();
        }
        catch (Exception e) {
            return null;
        }
    }

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
                .placeId(store.getId())
                .placeTitle(store.getName())
                .placeAddress(store.getAddress())
                .placeType(store.getType())
                .placeImgsUrl(store.getImages())
                .menus(menuList)
                .timeInfo(processTimeInfo(store.getTime()))
                .latitude(formatCoordinate(store.getMapx()))
                .longitude(formatCoordinate(store.getMapy()))
                .build();
    }
    private static String formatCoordinate(String coordinate) {
        double value = Double.parseDouble(coordinate) / 10000000.0;
        return String.format("%.7f", value);

    }
}
