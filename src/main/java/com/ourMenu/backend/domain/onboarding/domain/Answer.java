package com.ourMenu.backend.domain.onboarding.domain;

import com.ourMenu.backend.domain.onboarding.util.S3Util;
import lombok.Getter;

import java.util.List;

@Getter
public enum Answer {
    FEELING("좋아!", S3Util.S3Path + "좋아.svg", AnswerImg.LIKE, AnswerFood.LIKE,
            "별로야..", S3Util.S3Path + "별로야.svg", AnswerImg.DISLIKE, AnswerFood.DISLIKE),
    WEATHER("맑아", S3Util.S3Path + "맑아.svg", AnswerImg.SUNNY, AnswerFood.SUNNY,
            "비가 오네", S3Util.S3Path + "비가와.svg", AnswerImg.RAINY, AnswerFood.RAINY),
    STRESS("달달한 음식을 먹어", S3Util.S3Path + "달달한음식.svg", AnswerImg.SWEET, AnswerFood.SWEET,
            "매운 음식이지!", S3Util.S3Path + "매운음식.svg", AnswerImg.SPICY, AnswerFood.SPICY),
    TRAVEL("바다", S3Util.S3Path + "바다.svg", AnswerImg.SEA, AnswerFood.SEA,
            "산", S3Util.S3Path + "산.svg", AnswerImg.MOUNTAIN, AnswerFood.MOUNTAIN),
    SEASON("여름", S3Util.S3Path + "여름.svg", AnswerImg.SUMMER, AnswerFood.SUMMER,
            "겨울", S3Util.S3Path + "겨울.svg", AnswerImg.WINTER, AnswerFood.WINTER),
    ;

    private final String yes;
    private final String yesImg;
    private final AnswerImg yesAnswerImg;
    private final AnswerFood yesAnswerFood;
    private final String no;
    private final String noImg;
    private final AnswerImg noAnswerImg;
    private final AnswerFood noAnswerFood;

    Answer(String yes, String yesImg, AnswerImg yesAnswerImg, AnswerFood yesAnswerFood,
           String no, String noImg, AnswerImg noAnswerImg, AnswerFood noAnswerFood) {
        this.yes = yes;
        this.yesImg = yesImg;
        this.yesAnswerImg = yesAnswerImg;
        this.yesAnswerFood = yesAnswerFood;
        this.no = no;
        this.noImg = noImg;
        this.noAnswerImg = noAnswerImg;
        this.noAnswerFood = noAnswerFood;
    }

    public List<String> getAnswerFood(AnswerType answerType) {
        if (answerType.equals(AnswerType.YES)) {
            return yesAnswerFood.getStringList();
        }
        return noAnswerFood.getStringList();
    }


}
