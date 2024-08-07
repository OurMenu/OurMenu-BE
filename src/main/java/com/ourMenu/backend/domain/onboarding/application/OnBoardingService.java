package com.ourMenu.backend.domain.onboarding.application;

import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import com.ourMenu.backend.domain.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnBoardingService {

    private final MenuRepository menuRepository;

    public List<Question> getAllQuestion(){
        return Question.getAllQuestions();
    }

    public List<Store> findStoreByQuestionAnswer(int questionId, AnswerType answerType){
        List<String> foodStringList = Question.getAnswerFoodByIdAndAnswerType(questionId, answerType);
        return Collections.emptyList();
    }


}
