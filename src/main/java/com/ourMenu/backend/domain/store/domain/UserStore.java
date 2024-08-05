package com.ourMenu.backend.domain.store.domain;

import com.ourMenu.backend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String storeId;

    private String storeName;

    private String address;

    private LocalDateTime modifiedAt;

    public static UserStore toEntity(Store store,Long userId){
        return UserStore.builder()
                .userId(userId)
                .storeId(store.getId())
                .storeName(store.getName())
                .address(store.getAddress())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public UserStore updateModifiedAt(){
        modifiedAt= LocalDateTime.now();
        return this;
    }
}
