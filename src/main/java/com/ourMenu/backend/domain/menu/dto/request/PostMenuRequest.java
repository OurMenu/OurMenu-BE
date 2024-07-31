package com.ourMenu.backend.domain.menu.dto.request;

import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMenuRequest {

    private String title;
    private int price;
    private String ImgUrl;
    private String memo;
}
