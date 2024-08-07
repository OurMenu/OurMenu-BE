package com.ourMenu.backend.domain.onboarding.application;

import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import com.ourMenu.backend.domain.onboarding.util.S3Util;
import com.ourMenu.backend.domain.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnBoardingService {

    private final MenuRepository menuRepository;

    public List<Question> getAllQuestion() {
        return Question.getAllQuestions();
    }

    public List<Menu> findStoreByQuestionAnswer(Long userId, int questionId, AnswerType answerType) {
        List<String> foodStringList = Question.getAnswerFoodByIdAndAnswerType(questionId, answerType);
        String regexp = S3Util.makeRegexp(foodStringList);
        return menuRepository.findMenusByRegexp(regexp, userId);
    }


}
