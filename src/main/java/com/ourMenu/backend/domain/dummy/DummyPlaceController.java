package com.ourMenu.backend.domain.dummy;

import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dummy")
public class DummyPlaceController {


    @GetMapping("/place")
    public ApiResponse<List<Place>> getAllplace(){
        List<Place> places = new ArrayList<>();
        places.add(new Place(1, "화산라멘 홍대점", 3, 1269258716, 375531589));
        places.add(new Place(2, "화산라멘 강남점", 3, 1269258716, 375531589));
        places.add(new Place(3, "화산라멘 신촌점", 3, 1269258716, 375531589));
        return ApiUtils.success(places);
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Place {
        private int placeId;
        private String placeName;
        private int icon; // 프론트에서 저장됨
        private double latitude;
        private double longitude;
    }

    @GetMapping("/place/info")
    public ApiResponse<findOneplaceDto> findOneplace(@RequestParam String title){
        List<MenuImage> menuImages = new ArrayList<>();
        menuImages.add(new MenuImage("https://example.com/image1.jpg"));
        menuImages.add(new MenuImage("https://example.com/image2.jpg"));

        // 더미 메뉴 객체 생성
        Menu menu = new Menu(menuImages, "화산돈부리", "화산라멘 홍대점");
        menu.setMenuImgs(menuImages);
        menu.setMenuTitle("화산돈부리");
        menu.setStoreName("화산라멘 홍대점");

        // DTO 객체 생성 및 설정
        findOneplaceDto findOnePlaceDto = new findOneplaceDto(menu);

        return ApiUtils.success(findOnePlaceDto);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class findOneplaceDto {
        private Menu menu;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Menu {
        private List<MenuImage> menuImgs;
        private String menuTitle;
        private String storeName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuImage {
        private String imgUrl;
    }

    public static class StoreTitle {
        private String title;
    }

}
