package com.ourMenu.backend.domain.menulist.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostMenuListRequest {
    private String imgUrl;
    private String title;
}
