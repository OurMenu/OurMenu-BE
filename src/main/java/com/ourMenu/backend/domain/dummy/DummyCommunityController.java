package com.ourMenu.backend.domain.dummy;

import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("dummy")
public class DummyCommunityController {

    @GetMapping("/community")
    public ApiResponse<List<GetCommunity>> getCommunity(){
        GetCommunity getCommunity1=new GetCommunity(1,"제목이에요","내용이예요내용이예요내용이예요내용이예요내용이예요내용이예요내용이"
        ,"김시진","https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80.jpg",7,123
                ,"https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%EC%84%AC%EB%84%A4%EC%9D%BC.png");
        GetCommunity getCommunity2=new GetCommunity(2,"제목이에요","내용이예요내용이예요내용이예요내용이예요내용이예요내용이예요내용이"
                ,"김시진","https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80.jpg",7,123
                ,"https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%EC%84%AC%EB%84%A4%EC%9D%BC.png");
        List<GetCommunity> getCommunity = new ArrayList<>();
        getCommunity.add(getCommunity1);
        getCommunity.add(getCommunity2);

        return ApiUtils.success(getCommunity);
    }


    @AllArgsConstructor
    @Data
    private static class GetCommunity{
        private int id;
        private String title;
        private String content;
        private String creator;
        private String profileImgUrl;
        private int menusCount;
        private int views;
        private String thumbnail;


    }
}
