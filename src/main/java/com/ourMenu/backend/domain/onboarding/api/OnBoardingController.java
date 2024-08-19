package com.ourMenu.backend.domain.onboarding.api;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.onboarding.api.response.GetOnboardingResponse;
import com.ourMenu.backend.domain.onboarding.api.response.GetOnboardingState;
import com.ourMenu.backend.domain.onboarding.api.response.GetQuestionRecommands;
import com.ourMenu.backend.domain.onboarding.api.response.GetTagRecommends;
import com.ourMenu.backend.domain.onboarding.application.OnBoardingService;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.DefaultTag;
import com.ourMenu.backend.domain.onboarding.domain.OnBoardingState;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import com.ourMenu.backend.domain.onboarding.util.S3Util;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding")
public class OnBoardingController {

    private final OnBoardingService onBoardService;

    @GetMapping
    public ApiResponse<List<GetOnboardingResponse>> getOnboarding() {
        List<Question> allQuestion = onBoardService.getAllQuestion();
        List<GetOnboardingResponse> list = allQuestion.stream().map(GetOnboardingResponse::toDto).toList();
        return ApiUtils.success(list);
    }

    @GetMapping("/recommend")
    public ApiResponse<GetQuestionRecommands> getQuestionRecommend(@RequestParam(value = "questionId", required = false) Integer questionId,
                                                                   @RequestParam(value = "answer", required = false) AnswerType answerType,
                                                                   @UserId Long userId) {
        if(questionId == null) {
            questionId = S3Util.getRandomQuestionId();
        }
        if(answerType == null){
            answerType = S3Util.getRandomAnswerType();
        }
        List<Menu> menuList = new ArrayList<>();
        menuList.addAll(onBoardService.saveAndFindStoreByQuestionAnswer(userId, questionId, answerType));
        int boundary = menuList.size() - 1;

        menuList.addAll(onBoardService.findOtherUserMenusByQuestionAnswer(userId, questionId, answerType));

        boundary = min(boundary, 14);
        if (menuList.size() > 15) {
            menuList = menuList.subList(0, 15);
        }

        return ApiUtils.success(GetQuestionRecommands.toDto(menuList, questionId, answerType, boundary));

    }

    @GetMapping("/recommend/tag")
    public ApiResponse<List<GetTagRecommends>> getQuestionRecommend(@UserId Long userId) {
        List<DefaultTag> defaultTagList = DefaultTag.getRandomTag(2);

        List<GetTagRecommends> getTagRecommendsList = new ArrayList<>();
        for (DefaultTag defaultTag : defaultTagList) {
            List<Menu> menuList1 = onBoardService.findStoreByRandomTag(userId, defaultTag);
            getTagRecommendsList.add(GetTagRecommends.toDtoOwn(menuList1, defaultTag));

            List<Menu> menuList2 = onBoardService.findOtherStoreByRandomTag(userId, defaultTag);
            getTagRecommendsList.get(getTagRecommendsList.size()-1).addAll(menuList2);
        }

        if (getTagRecommendsList.size() > 15) {
            getTagRecommendsList = getTagRecommendsList.subList(0, 15);
        }
        return ApiUtils.success(getTagRecommendsList);
    }

    @GetMapping("/state")
    public ApiResponse<GetOnboardingState> getOnboardingState(@UserId Long userId) {
        OnBoardingState onBoardingState = onBoardService.findOneById(userId);
        return ApiUtils.success(GetOnboardingState.toDto(onBoardingState));
    }
}
