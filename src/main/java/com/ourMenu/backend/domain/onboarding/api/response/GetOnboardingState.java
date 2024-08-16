package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.OnBoardingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class GetOnboardingState {

    private int questionId;

    private AnswerType answerType;

    public static GetOnboardingState toDto(OnBoardingState onBoardingState) {
        return GetOnboardingState.builder()
                .questionId(onBoardingState.getQuestionId())
                .answerType(onBoardingState.getAnswerType())
                .build();
    }
}
