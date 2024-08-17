package com.ourMenu.backend.domain.article.domain;

import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.menu.domain.MenuImage;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(of = {"id", "title", "price","placeTitle","address","menuImage"})
public class ArticleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_menu_id")
    private Long id;

    @ToStringExclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    //메뉴 이름
    private String title;

    //메뉴 가격
    private int price;

    //메뉴 메모
    private String placeTitle;

    //가게 주소
    private String address;

    //이미지
    private String imgUrl;

    //그룹 ID
    private Long groupId;

    @Builder.Default
    private int sharedCount = 0;

    //메모 타이틀?
    private String menuMemoTitle;

    //아이콘 타입
    private String menuIconType;

    //가게 영업 시간
    private String placeMemo;

    //가게 위도
    private double placeLatitude;

    //가게 경도
    private double placeLongitude;

    public void confirmArticle(Article article){
        this.article = article;
        article.upMenuCount();
    }

    public void deleteArticle(){
        this.article = null;
    }
    public void addSharedCount(){ this.sharedCount++; }
}
