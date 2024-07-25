package com.ourMenu.backend.domain.menu.dto.request;

import lombok.Data;

@Data
public class PatchMenuRequest {
    private String title;
    private int price;
    private String ImgUrl;
    private String memo;
}
