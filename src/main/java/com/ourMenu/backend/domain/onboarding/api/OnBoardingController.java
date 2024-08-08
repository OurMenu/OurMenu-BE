package com.ourMenu.backend.domain.onboarding.api;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.onboarding.api.response.GetOnboardingResponse;
import com.ourMenu.backend.domain.onboarding.api.response.GetQuestionRecommands;
import com.ourMenu.backend.domain.onboarding.application.OnBoardingService;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/recommand")
    public ApiResponse<GetQuestionRecommands> getQuestionRecommand(@RequestParam("questionId") int questionId,
                                            @RequestParam("answer") AnswerType answerType,
                                            @UserId Long userId) {
        List<Menu> menus = onBoardService.findStoreByQuestionAnswer(userId, questionId, answerType);
        return ApiUtils.success(GetQuestionRecommands.toDto(menus,questionId,answerType));

    }
}
