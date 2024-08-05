package com.ourMenu.backend.domain.onboarding.application;

import com.ourMenu.backend.domain.onboarding.domain.Answer;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnBoardService {

    public List<Question> getAllQuestion(){
        return Question.getAllQuestions();
    }
}
