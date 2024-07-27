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
    public ApiResponse<GetPlaceInfo> findOneplace(@RequestParam String title){

        Menu menu1=new Menu("(제주산生)특등심돈카츠(1인분)",11900);
        Menu menu2=new Menu("통뼈 토마호크돈카츠",14900);
        Menu menu3=new Menu("모짜쭈욱 치즈돈카츠(1인분)",12900);
        Menu menu4=new Menu("소고기쌀국수",8900);
        List<Menu> menuList1=List.of(menu1,menu2,menu3,menu4);


        MenuImage menuImage1=new MenuImage("https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220823_50%2F16612301409395DMNF_JPEG%2FIMG_3833-2.jpg\"");
        MenuImage menuImage2=new MenuImage("https://search.pstatic.net/\"https://search.pstatic.net/common/?autoRotate=true&type=w278_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220823_59%2F1661230140565sNVpR_JPEG%2FKakaoTalk_20211130_221922417_04.jpg\"");
        MenuImage menuImage3=new MenuImage("https://search.pstatic.net/common/?autoRotate=true&type=w278_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220823_179%2F1661230140874CvVXo_JPEG%2FIMG_7366.jpg");
        List<MenuImage>menuImageList1=List.of(menuImage1,menuImage2,menuImage3);
        FindOneplaceDto findOneplaceDto1=new FindOneplaceDto("오유미당 건대점","서울특별시 광진구","한식",menuImageList1,menuList1);

        Menu menu5=new Menu("문어&족발 반반 냉채",36000);
        Menu menu6=new Menu("문어 냉채",28000);
        Menu menu7=new Menu("문어 숙회",28000);
        Menu menu8=new Menu("냉채 족발",26000);
        List<Menu> menuList2=List.of(menu5,menu6,menu7,menu8);

        MenuImage menuImage4=new MenuImage("https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230504_61%2F16831806545901VlsD_JPEG%2F20230429_004808.jpg");
        MenuImage menuImage5=new MenuImage("https://search.pstatic.net/common/?autoRotate=true&type=w278_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20231025_213%2F1698217814854pw1Uy_JPEG%2F20231024_210233-EDIT.jpg");
        MenuImage menuImage6=new MenuImage("https://search.pstatic.net/common/?autoRotate=true&type=w278_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20231026_110%2F1698303966223WV2h6_JPEG%2FIMG_20230914_002214_222.jpg");
        List<MenuImage>menuImageList2=List.of(menuImage4,menuImage5,menuImage6);
        FindOneplaceDto findOneplaceDto2=new FindOneplaceDto("포차브루다","서울특별시 광진구","한식",menuImageList2,menuList2);
        return ApiUtils.success(new GetPlaceInfo(List.of(findOneplaceDto1,findOneplaceDto2)));
    }

    @Data
    @AllArgsConstructor
    private class GetPlaceInfo{
        List<FindOneplaceDto> findOneplaceDtos;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class FindOneplaceDto {
        private String name;
        private String address;
        private String type;
        private List<MenuImage> images;
        private List<Menu> menu;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Menu {
        private String name;
        private int price;
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
