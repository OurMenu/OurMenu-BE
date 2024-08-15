package com.ourMenu.backend.domain.onboarding.domain;

import com.ourMenu.backend.domain.onboarding.util.S3Util;
import lombok.Getter;

import java.util.List;

@Getter
public enum Answer {
    FEELING("좋아!", "좋아", AnswerImg.LIKE, AnswerFood.LIKE,
            "별로야..", "별로야", AnswerImg.DISLIKE, AnswerFood.DISLIKE),
    WEATHER("맑아", "맑아", AnswerImg.SUNNY, AnswerFood.SUNNY,
            "비가 오네", "비가와", AnswerImg.RAINY, AnswerFood.RAINY),
    STRESS("달달한 음식을 먹어", "달달한음식", AnswerImg.SWEET, AnswerFood.SWEET,
            "매운 음식이지!", "매운음식", AnswerImg.SPICY, AnswerFood.SPICY),
    TRAVEL("바다", "바다", AnswerImg.SEA, AnswerFood.SEA,
            "산", "산", AnswerImg.MOUNTAIN, AnswerFood.MOUNTAIN),
    SEASON("여름", "여름", AnswerImg.SUMMER, AnswerFood.SUMMER,
            "겨울", "겨울", AnswerImg.WINTER, AnswerFood.WINTER),
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

    public String getYesImg() {
        return S3Util.getS3SvgPath(yesImg);
    }

    public String getNoImg() {
        return S3Util.getS3SvgPath(noImg);
    }

}
