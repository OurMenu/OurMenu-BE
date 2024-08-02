package com.ourMenu.backend.domain.dummy;

import lombok.*;

@Data
@Getter @Setter
@AllArgsConstructor
public class DummyMenuFolderDto {
    private String title;
    private int menuCount;
    private String imgUrl;
    private int iconType;
}
