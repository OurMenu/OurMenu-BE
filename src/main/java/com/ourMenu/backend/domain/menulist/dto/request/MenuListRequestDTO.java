package com.ourMenu.backend.domain.menulist.dto.request;

 import lombok.Builder;
 import lombok.Data;
 import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class MenuListRequestDTO {
//    private String img;
    private MultipartFile menuFolderImg;    //파일로 받아야 함
    private String menuFolderTitle;
    private String menuFolderIcon;
}
