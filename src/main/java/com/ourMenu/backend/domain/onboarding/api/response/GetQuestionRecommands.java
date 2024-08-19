package com.ourMenu.backend.domain.onboarding.api.response;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GetQuestionRecommands {

    private String recommendImgUrl;
    private List<GetRecommend> menus;

    public static GetQuestionRecommands toDto(List<Menu> menus, int questionId, AnswerType answerType,int boundary) {
        List<GetRecommend> recommandList = new ArrayList<>();
        for(int i=0 ;i<menus.size();i++){
            boolean isUserOwned = false;
            if(i<=boundary)
                isUserOwned = true;
            GetRecommend getRecommend = GetRecommend.toDto(menus.get(i), isUserOwned);
            recommandList.add(getRecommend);
        }

        String recommandImgUrl = Question.getImgUrlByIdAndAnswerType(questionId, answerType);
        return GetQuestionRecommands.builder()
                .recommendImgUrl(recommandImgUrl)
                .menus(recommandList)
                .build();

    }
}
