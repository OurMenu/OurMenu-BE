package com.ourMenu.backend.domain.onboarding.domain;

import com.ourMenu.backend.domain.onboarding.util.S3Util;

import java.util.*;

public enum AnswerImg {

    LIKE("기분이 좋을 땐 친구들과 약속 어때요1", "기분이 좋을 땐 친구들과 약속 어때요2"),
    DISLIKE("저기압일 땐 지금 당장 고기앞으로 가라", "기분이 꿀꿀할 때는 돼지파티다"),
    SUNNY("맑은 라인 오늘 바깥에서 피크닉 어때요"),
    RAINY("비오고 쌀쌀할때 뜨끈한 국물 어때요"),
    SWEET("스트레스 해소엔 달달한게 최고지", "스트레스 만땅 달달함으로 풀어볼까요"),
    SPICY("스트레스 해소엔 역시 불닭으로 화끈하게"),
    SEA("바다 내음이 물씬 풍기는 해산물 어때요1", "바다 내음이 물씬 풍기는 해산물 어때요2"),
    MOUNTAIN("피톤치드 향 가득 느낄 수 있는 건강식 어때요", "무더운 여름엔 이열치열로 땀좀 빼 볼까요"),
    SUMMER("따가운 햇살엔 이열치열! 뜨거운 음식들", "햇살이 뜨거울때 차가운 음식들로 이겨내 볼까요"),
    WINTER("추운 겨울엔 뜨끈한 국물 어때요", "추운 겨울의 따끈따근한 제철 음식이에요"),
    ;

    private final List<String> imgUrlList = new ArrayList<>();

    AnswerImg(String... strings) {
        imgUrlList.addAll(Arrays.asList(strings));
    }

    public String getRandomImgUrl() {
        Random random = new Random();
        int randomValue = random.nextInt(imgUrlList.size());
        return S3Util.S3Path + imgUrlList.get(randomValue) + ".svg";
    }
}
