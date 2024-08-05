package com.ourMenu.backend.domain.onboarding.domain;

public enum Answer {
    DUMMY("1","1","1","1","1","1");

    private String yes;
    private String yesImg;
    private String yesAnswerImg;
    private String no;
    private String noImg;
    private String noAnswerImg;

    Answer(String yes, String yesImg, String yesAnswerImg, String no, String noImg, String noAnswerImg) {
        this.yes = yes;
        this.yesImg = yesImg;
        this.yesAnswerImg = yesAnswerImg;
        this.no = no;
        this.noImg = noImg;
        this.noAnswerImg = noAnswerImg;
    }
}
