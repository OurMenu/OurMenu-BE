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

    private String title;

    private int price;

    private String placeTitle;

    private String address;

    private String imgUrl;

    private Long groupId;

    public void confirmArticle(Article article){
        this.article = article;
        article.upMenuCount();
    }

    public void deleteArticle(){
        this.article = null;
    }
}
