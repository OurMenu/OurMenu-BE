package com.ourMenu.backend.domain.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class DummyMenuFolderRequest {
    private String title;
    private String img;
    private Long iconType;
}
