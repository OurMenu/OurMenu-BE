package com.ourMenu.backend.domain.onboarding.api;

import com.ourMenu.backend.domain.onboarding.api.response.GetOnboardingResponse;
import com.ourMenu.backend.domain.onboarding.application.OnBoardingService;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import com.ourMenu.backend.global.argument_resolver.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OnBoardingController {

    private final OnBoardingService onBoardService;

    @GetMapping("/onboarding")
    public List<GetOnboardingResponse> getOnboarding() {
        List<Question> allQuestion = onBoardService.getAllQuestion();
        return allQuestion.stream().map(GetOnboardingResponse::toDto).toList();

    }

    @GetMapping("/home/recommand")
    public String getQuestionRecommand(@RequestParam("questionId") int questionId,
                                       @RequestParam("answer") AnswerType answerType,
                                       @UserId Long userId) {
        onBoardService.findStoreByQuestionAnswer(questionId, answerType);
        return "success";
    }
}
