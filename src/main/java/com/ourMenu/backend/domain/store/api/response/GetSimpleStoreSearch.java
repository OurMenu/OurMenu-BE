package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.Store;
import com.ourMenu.backend.domain.store.domain.UserStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class GetSimpleStoreSearch {

    private String placeId;

    private String placeTitle;

    private String placeAddress;

    public static GetSimpleStoreSearch toDto(Store store){
        return GetSimpleStoreSearch.builder()
                .placeId(store.getId())
                .placeTitle(store.getName())
                .placeAddress(store.getAddress())
                .build();
    }

}
