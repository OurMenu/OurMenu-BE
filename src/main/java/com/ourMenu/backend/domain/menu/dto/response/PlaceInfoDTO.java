package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceInfoDTO {
    private String placeAddress;
    private String placeInfo;

    public static PlaceInfoDTO toDto(Place place){
        return PlaceInfoDTO.builder()
                .placeAddress(place.getAddress())
                .placeInfo(place.getInfo())
                .build();
    }
}
