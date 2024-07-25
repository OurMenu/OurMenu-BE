package com.ourMenu.backend.domain.menu.dto.response;

import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
public class GetMenuResponse {
    private Long id;
    private String title;
    private int price;
    private String imgUrl;
    private MenuStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String memo;
}
