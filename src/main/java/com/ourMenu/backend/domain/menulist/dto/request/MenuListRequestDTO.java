package com.ourMenu.backend.domain.menulist.dto.request;

 import lombok.Builder;
 import lombok.Data;
 import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class MenuListRequestDTO {
    private String img;
    private String title;
    private String iconType;
}
