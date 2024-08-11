package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GetQuestionRecommands {

    private String recommendImgUrl;
    private List<GetRecommend> menus;

    public static GetQuestionRecommands toDto(List<Menu> menus, int questionId, AnswerType answerType) {
        List<GetRecommend> recommandList = menus.stream().map(GetRecommend::toDto).toList();
        String recommandImgUrl = Question.getImgUrlByIdAndAnswerType(questionId, answerType);
        return GetQuestionRecommands.builder()
                .recommendImgUrl(recommandImgUrl)
                .menus(recommandList)
                .build();

    }
}
