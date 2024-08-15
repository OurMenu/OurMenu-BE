package com.ourMenu.backend.domain.onboarding.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Question {

    FEELING(1, "오늘 기분은 어떠신가요?", Answer.FEELING),
    WEATHER(2, "오늘 날씨는 어떤가요?", Answer.WEATHER),
    STRESS(3, "스트레스 받을 때는 어떤 음식을 드시나요?", Answer.STRESS),
    TRAVEL(4, "어디로 떠나고 싶은가요?", Answer.TRAVEL),
    SEASON(5, "어느 계절을 더 좋아하세요?", Answer.SEASON);

    private final int id;
    private final String question;
    private final Answer answer;


    Question(int id, String question, Answer answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public static List<Question> getAllQuestions() {
        return Arrays.stream(Question.values()).toList();
    }

    public static String getImgUrlByIdAndAnswerType(int id, AnswerType answerType) {
        Question question = getQuestionByIdAndAnswerType(id, answerType);
        if(answerType.equals(AnswerType.YES))
            return question.getAnswer().getYesAnswerImg().getRandomImgUrl();
        return question.getAnswer().getNoAnswerImg().getRandomImgUrl();

    }

    public static Question getQuestionByIdAndAnswerType(int id, AnswerType answerType) {
        for (Question question : values()) {
            if (question.getId() == id) {
                return question;
            }
        }
        throw new RuntimeException("questionId에 해당하는 값이 없습니다");
    }

    public static List<String> getAnswerFoodByIdAndAnswerType(int id, AnswerType answerType) {
        return getQuestionByIdAndAnswerType(id, answerType).getAnswer().getAnswerFood(answerType);

    }
}
