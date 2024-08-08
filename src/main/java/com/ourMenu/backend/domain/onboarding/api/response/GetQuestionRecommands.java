package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class GetQuestionRecommands {

    private String recommandImgUrl;
    private List<GetRecommand> menus;

    public static GetQuestionRecommands toDto(List<Menu> menus, int questionId, AnswerType answerType) {
        List<GetRecommand> recommandList = menus.stream().map(GetRecommand::toDto).toList();
        String recommandImgUrl = Question.getImgUrlByIdAndAnswerType(questionId, answerType);
        return GetQuestionRecommands.builder()
                .recommandImgUrl(recommandImgUrl)
                .menus(recommandList)
                .build();

    }
}
