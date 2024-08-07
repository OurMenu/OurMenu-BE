package com.ourMenu.backend.domain.menu.api.response;

import com.ourMenu.backend.domain.menu.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetPlaceResponse {

    private Long placeId;
    private String placeTitle;
    private String menuIcon;
    private Double latitude;
    private Double longitude;

    public static GetPlaceResponse toDto(Place place){
        return GetPlaceResponse.builder()
                .placeId(place.getId())
                .placeTitle(place.getTitle())
                .menuIcon(place.getMenus().get(0).getIcon())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }
}
