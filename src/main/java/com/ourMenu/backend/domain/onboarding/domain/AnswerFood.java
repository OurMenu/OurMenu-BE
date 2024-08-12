package com.ourMenu.backend.domain.onboarding.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum AnswerFood {

    LIKE("파스타", "삼겹살", "피자", "까르보나라", "스파게티", "알리오올리오", "목살", "항정살", "볼로네제"),
    DISLIKE("떡볶이", "닭발", "짬뽕", "닭갈비", "낙곱새", "매운 갈비찜", "갈비찜", "찜갈비"),
    SUNNY("치킨", "피자", "고르곤졸라", "마르게리타", "볼케이노", "갈비천왕", "후라이드", "닭강정", "허니콤보"),
    RAINNY("파전", "막걸리", "칼국수", "수제비", "김치전"),
    SWEET("디저트", "카페", "빵", "케이크"),
    SPICY("떡볶이", "닭발", "짬뽕", "닭갈비", "낙곱새", "매운 갈비찜", "갈비찜", "찜갈비"),
    SEA("회", "스시", "조개탕", "게장", "칼국수", "생선구이"),
    MOUNTAIN("비빔밥", "닭백숙", "닭볶음탕", "파전", "김밥", "전"),
    SUMMER("아이스크림", "냉면", "초계국수", "콩국수", "삼계탕"),
    WINNTER("부대찌개", "설렁탕", "감자탕", "어묵", "국밥"),

    ;

    private final List<String> stringList = new ArrayList<>();

    AnswerFood(String... strList) {
        stringList.addAll(Arrays.asList(strList));
    }
}
