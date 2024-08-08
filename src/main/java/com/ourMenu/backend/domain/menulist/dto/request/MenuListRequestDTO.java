package com.ourMenu.backend.domain.menulist.dto.request;

 import lombok.Builder;
 import lombok.Data;
 import org.springframework.web.multipart.MultipartFile;
 import java.util.List;
 import java.util.Optional;

@Data
@Builder
public class MenuListRequestDTO {
//    private String img;
    private Optional<MultipartFile> menuFolderImg;    //파일로 받아야 함
    private String menuFolderTitle;
    private String menuFolderIcon;
    private List<Long> menuId;
}
