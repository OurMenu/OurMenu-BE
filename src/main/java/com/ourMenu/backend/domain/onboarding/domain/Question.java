package com.ourMenu.backend.domain.onboarding.domain;

public enum Question {

    FEELING(1,"오늘 기분은 어떠신가요?",Answer.DUMMY),
    WEATHER(2,"오늘 날씨는 어떤가요?",Answer.DUMMY),
    STRESS(3,"스트레스 받을 때는 어떤 음식을 드시나요?",Answer.DUMMY),
    TRAVEL(4,"어디로 떠나고 싶은가요?",Answer.DUMMY),
    SEASON(5,"어느 계절을 더 좋아하세요?",Answer.DUMMY);


    private int id;
    private String question;
    private Answer answer;


    Question(int id, String question, Answer answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

}
