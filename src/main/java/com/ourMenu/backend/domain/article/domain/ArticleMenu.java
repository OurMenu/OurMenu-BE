package com.ourMenu.backend.domain.article.domain;

import com.ourMenu.backend.domain.article.domain.Article;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
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
    private Long price;

    private String image;

    public void confirmArticle(Article article){
        this.article = article;
        article.addArticleMenu(this);
    }
}
