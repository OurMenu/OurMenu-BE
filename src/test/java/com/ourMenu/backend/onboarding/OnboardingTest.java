package com.ourMenu.backend.onboarding;

import com.ourMenu.backend.domain.onboarding.application.OnBoardingService;
import com.ourMenu.backend.domain.onboarding.domain.Answer;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OnboardingTest {

    @Autowired
    OnBoardingService onBoardingService;

    @Test
    @DisplayName("questionId와 AnswerType 에 알맞는 단어를 가져온다")
    public void test1(){
        int questionId=1;
        AnswerType answerType=AnswerType.YES;
        List<String> stringList = Question.getAnswerFoodByIdAndAnswerType(questionId, answerType);
        for (String foodName : stringList) {

        }
    }
}
