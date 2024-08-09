package com.ourMenu.backend.domain.menulist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
public class PatchMenuListRequestDTO {
    @Schema(description = "메뉴판 이미지 파일", nullable = true)
    private MultipartFile menuFolderImg;    //파일로 받아야 함
    private String menuFolderTitle;
    private String menuFolderIcon;
}
