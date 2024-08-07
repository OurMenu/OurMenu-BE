package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.UserStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class GetSearchHistory {

    private String placeTitle;

    private String placeAddress;

    private LocalDateTime modifiedAt;

    public static GetSearchHistory toDto(UserStore userStore){
        return GetSearchHistory.builder()
                .placeTitle(userStore.getStoreName())
                .placeAddress(userStore.getAddress())
                .modifiedAt(userStore.getModifiedAt())
                .build();
    }
}
