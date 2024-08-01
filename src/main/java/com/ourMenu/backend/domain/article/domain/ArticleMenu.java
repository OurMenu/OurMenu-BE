package com.ourMenu.backend.domain.article.domain;

import com.ourMenu.backend.domain.article.domain.Article;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private String title;
    private Long price;

    private String image;

    public void confirmArticle(Article article){
        this.article = article;
        article.addArticleMenu(this);
    }
}
