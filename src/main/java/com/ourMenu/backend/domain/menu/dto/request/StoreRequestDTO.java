package com.ourMenu.backend.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequestDTO {
    private String storeName;
    private String storeMemo;
    private String storeAddress;
    private Double storeLatitude;  // 위도
    private Double storeLongitude; // 경도
}
