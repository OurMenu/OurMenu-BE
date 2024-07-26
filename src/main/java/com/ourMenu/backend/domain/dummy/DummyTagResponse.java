package com.ourMenu.backend.domain.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DummyTagResponse {
    private String menuTitle;
    private String storeName;
    private String imgUrl;
}
