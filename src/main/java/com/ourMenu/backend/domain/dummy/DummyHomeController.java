package com.ourMenu.backend.domain.dummy;

import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dummy")
public class DummyHomeController {

    //    			"questionId": 1
//                        "question": "오늘 기분은 어떠신가요?",
//                        "yes": "좋아!",
//                        "yesImg":"좋아 이미지",
//                        "yesAnswerUrl":"이미지url",
//                        "no": "별로야..",
//                        "noImg":"좋아 이미지",
//                        "noAnswerUrl":"이미지url",

    @GetMapping("/onboarding")
    public ApiResponse<DummyOnboradingResponse> dummyGetOnboarding(){
        DummyOnboradingResponse response = new DummyOnboradingResponse(1L, "오늘 기분은 어떠신가요?", "좋아!"
                , "https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%EC%A2%8B%EC%95%84%EC%9A%94.png", "\"https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%EC%8A%A4%ED%8A%B8%EB%A0%88%EC%8A%A4+%EB%B0%9B%EC%9D%84+%EB%95%8C+%EB%A7%A4%EC%BD%A4%ED%95%9C+%EC%9D%8C%EC%8B%9D+%EC%B6%94%EC%B2%9C+%EB%93%9C%EB%A0%A4%EC%9A%94.png\"",
                "별로야..", "https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%EC%8B%AB%EC%96%B4%EC%9A%94.png", "https://hobbytat.s3.ap-northeast-2.amazonaws.com/dump/%EC%8A%A4%ED%8A%B8%EB%A0%88%EC%8A%A4+%EB%B0%9B%EC%9D%84+%EB%95%8C+%EB%A7%A4%EC%BD%A4%ED%95%9C+%EC%9D%8C%EC%8B%9D+%EC%B6%94%EC%B2%9C+%EB%93%9C%EB%A0%A4%EC%9A%94.png");
        return ApiUtils.success(response);
    }

    @GetMapping("/home/recommended/tag")
    public ApiResponse<List<DummyTagResponse>> dummyGetTag(){
        List<DummyTagResponse> responseList = new ArrayList<>();
        responseList.add(new DummyTagResponse("화산라멘", "화산라멘 멘야마쯔리 홍대점", "https://s3--urlpath"));
        responseList.add(new DummyTagResponse("화산라멘", "화산라멘 멘야마쯔리 홍대점", "https://s3--urlpath"));
        responseList.add(new DummyTagResponse("화산라멘", "화산라멘 멘야마쯔리 홍대점", "https://s3--urlpath"));

        return ApiUtils.success(responseList);
    }

    @GetMapping("/home/recommended/")
    public ApiResponse<DummyQnaResponse> dummyGetQna(){
        List<DummyQnaMenuDTO> list = new ArrayList<>();
        list.add(new DummyQnaMenuDTO("url","화산돈부리","화산라멘 홍대점"));
        list.add(new DummyQnaMenuDTO("url","화산돈부리","화산라멘 홍대점"));

        DummyQnaResponse response = new DummyQnaResponse("url", list);
        return ApiUtils.success(response);
    }
}
