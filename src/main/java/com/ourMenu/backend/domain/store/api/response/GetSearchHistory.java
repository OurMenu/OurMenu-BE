package com.ourMenu.backend.domain.store.api.response;

import com.ourMenu.backend.domain.store.domain.UserStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class GetSearchHistory {

    private String storeName;

    private String address;

    private LocalDateTime modifiedAt;

    public static GetSearchHistory toDto(UserStore userStore){
        return GetSearchHistory.builder()
                .storeName(userStore.getStoreName())
                .address(userStore.getAddress())
                .modifiedAt(userStore.getModifiedAt())
                .build();
    }
}
