package com.ourMenu.backend.domain.menulist.dto.request;

import lombok.Data;

@Data
public class PatchMenuListRequest {
    private String title;
    private String imgUrl;
}
