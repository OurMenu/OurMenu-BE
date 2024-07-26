package com.ourMenu.backend.domain.dummy;

import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class DummyController {

    @GetMapping("/menuFolder")
    public ApiResponse<List<DummyMenuFolderDto>> dummyGetMenu(){
        List<DummyMenuFolderDto> responseList = Arrays.asList(
                new DummyMenuFolderDto("서울숲 맛집", 10, "https://dummyimage.com/600x400/000/fff&text=서울숲+맛집", 1),
                new DummyMenuFolderDto("고기고기", 10, "https://dummyimage.com/600x400/000/fff&text=고기고기", 1),
                new DummyMenuFolderDto("홍대거리", 10, "https://dummyimage.com/600x400/000/fff&text=홍대거리", 1)
        );
        return ApiUtils.success(responseList);
    }

    @PostMapping("/menuFolder")
    public ApiResponse<DummyMenuFolderDto> dummySaveMenuFolder(@RequestBody DummyMenuFolderRequest dummyMenuFolderRequest){
        DummyMenuFolderDto response = new DummyMenuFolderDto(dummyMenuFolderRequest.getTitle(), 0, dummyMenuFolderRequest.getImg(), 1);
        return ApiUtils.success(response);
    }

    @PatchMapping("/menuFolder")
    public ApiResponse<String> dummyUpdateMenuFolder(@RequestBody UpdateMenuFolderDTO updateMenuFolderDTO){
        return ApiUtils.success("OK");
    }

    @GetMapping("/menu")
    public ApiResponse<DummyMenuDto> dummySaveMenu(){

        MenuFolderDTO menuFolderDTO = new MenuFolderDTO("한강 음식", "0");
        DummyMenuDto response = new DummyMenuDto(
                1,                                      // menuId
                "한강 라면",                              // menuTitle
                12000,                                  // price
                "한강 맛도리 음식",                       // memo
                "https://dummyimage.com/100x100/000/fff&text=라면", // icon
                Arrays.asList("https://dummyimage.com/600x400/000/fff&text=image1", "https://dummyimage.com/600x400/000/fff&text=image2"), // images
                menuFolderDTO// menuFolder
        );
        return ApiUtils.success(response);
    }

    @DeleteMapping("/menu/{id}")
    public ApiResponse<String> dummyRemoveMenu(@PathVariable Long id) {
        return ApiUtils.success("OK");
    }

    @DeleteMapping("/menuFolder/{id}")
    public ApiResponse<String> dummyRemoveMenuFolder(@PathVariable Long id){
        return ApiUtils.success("OK");
    }

    @PatchMapping("/menu")
    public ApiResponse<String> dummyUpdateMenu(@RequestBody UpdateMenuDTO updateMenuDTO){
        return ApiUtils.success("OK");
    }

    @Data
    @AllArgsConstructor
    public static class MenuFolderDTO {
        private String menuFolderTitle;
        private String icon;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMenuFolderDTO {
        private String title;
        private Long iconType;
        private String image;
    }

    @Data
    public static class UpdateMenuDTO{
        private String title;
        private int price;
        private String imgUrl;
        private String memo;
    }

    @Data
    @AllArgsConstructor
    public static class DummyDeleteDto{
        private Long id;
    }
}
