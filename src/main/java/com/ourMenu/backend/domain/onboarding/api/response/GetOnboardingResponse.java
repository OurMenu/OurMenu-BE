package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.onboarding.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetOnboardingResponse {

    private int questionId;
    private String question;
    private String yes;
    private String yesImg;
    private String yesAnswerUrl;
    private String no;
    private String noImg;
    private String noAnswerUrl;

    public static GetOnboardingResponse toDto(Question data) {
        return GetOnboardingResponse.builder()
                .questionId(data.getId())
                .question(data.getQuestion())
                .yes(data.getAnswer().getYes())
                .yesImg(data.getAnswer().getYesImg())
                .yesAnswerUrl(data.getAnswer().getYesAnswerImg().getRandomImgUrl())
                .no(data.getAnswer().getNo())
                .noImg(data.getAnswer().getNoImg())
                .noAnswerUrl(data.getAnswer().getNoAnswerImg().getRandomImgUrl())
                .build();
    }
}
