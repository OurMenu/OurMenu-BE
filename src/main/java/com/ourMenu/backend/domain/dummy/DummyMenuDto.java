package com.ourMenu.backend.domain.dummy;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DummyMenuDto {
    private int menuId;
    private String menuTitle;
    private int price;
    private String memo;
    private String icon;
    private List<String> images;
    private DummyController.MenuFolderDTO menuFolder;
}
