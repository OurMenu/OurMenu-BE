package com.ourMenu.backend.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPhotoRequest {
    private List<MultipartFile> imgs;
    private long menuId;
}
