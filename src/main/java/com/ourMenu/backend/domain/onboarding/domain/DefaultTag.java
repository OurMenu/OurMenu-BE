package com.ourMenu.backend.domain.onboarding.domain;

import com.ourMenu.backend.domain.onboarding.util.S3Util;
import lombok.Getter;

import java.util.*;

@Getter
public enum DefaultTag {

    KOREA("한식", "집밥이 그리울 땐,"),
    CHINA("중식", "짜장 먹을까? 짬뽕 먹을까?"),
    JAPAN("일식", "일본 여행이 가고 싶을 땐,"),
    WESTERN("양식", "기분 내고 싶은 오늘은,"),
    ASIA("아시안", "독특한 향을 느끼고 싶을 땐,"),
    RICE("밥", "밥이 먹고 싶을 땐,"),
    BREAD("빵", "빵이 먹고 싶을 땐,"),
    NOODLE("면", "면이 먹고 싶을 땐,"),
    MEAT("고기", "고기 구우러 가고 싶을 땐,"),
    FISH("생선", "부드러운 속살의 고소한 생선이 떠오를 땐,"),
    DESSERT("디저트", "달달한 디저트가 땡길 땐,"),
    CAFE("카페", "커피가 생각날 땐,"),
    FAST_FOOD("패스트푸드", "빠르고 맛있게!"),
    SPICY("매콤함", "스트레스 풀리는 매콤함,"),
    SWEET("달달함", "기분 좋아지는 달달함,"),
    COOL("시원함", "더위가 사라지는 시원함,"),
    HOT("뜨끈함", "땀나는 뜨끈함,"),
    HOT_SPICY("얼큰함", "얼큰함이 살아있는, "),
    SOLO("혼밥", "혼자 밥먹기 좋은 곳,"),
    WARM("뜨끈함", "땀나는 뜨끈함,"),
    BUSINESS("비즈니스미팅", "비즈니스미팅이 있을 땐,"),
    PROMISE("친구약속", "친구와 약속이 있다면?"),
    DATE("데이트", "데이트 하는 날엔,"),
    BUY_FOOD("밥약", "밥약하기 좋은 곳,"),
    ORGANIZATION("단체", "단체로 방문한다면,");

    private String TagName;
    private String TagMemo;

    DefaultTag(String tagName, String tagMemo) {
        TagName = tagName;
        TagMemo = tagMemo;
    }

    public static DefaultTag getRandomTag() {
        DefaultTag[] tags = DefaultTag.values();
        Random random = new Random();
        int randomValue = random.nextInt(25);
        return tags[randomValue];
    }

    public static List<DefaultTag> getRandomTag(int size) {
        if(size > S3Util.DEFAULT_TAG_SIZE)
            throw new RuntimeException("size개수가 총 태그 갯수보다 많습니다");

        DefaultTag[] tags = DefaultTag.values();
        Random random = new Random();
        Set<DefaultTag> set = new HashSet<>();

        while(set.size() < size){
            int randomValue = random.nextInt(25);
            if(set.contains(tags[randomValue]))
                continue;
            set.add(tags[randomValue]);
        }
        return set.stream().toList();
    }
}
