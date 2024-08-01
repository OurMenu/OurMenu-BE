package com.ourMenu.backend.article;

import com.ourMenu.backend.domain.article.application.ArticleService;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @DisplayName("save 로직에서 발생하는 쿼리수릃 확인한다.")
    @Test
    @Transactional
    public void test1() {
        //given
        ArticleMenu articleMenu1 = ArticleMenu.builder()
                .image("image1")
                .title("제목1")
                .price(1000L)
                .build();
        ArticleMenu articleMenu2 = ArticleMenu.builder()
                .image("image2")
                .title("제목2")
                .price(2000L)
                .build();
        List<ArticleMenu> articleMenuList = List.of(articleMenu1, articleMenu2);
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .articleMenuList(articleMenuList)
                .build();
        //when
        Article saveArticle = articleService.save(article);
        //then
        Assertions.assertThat(saveArticle.getId()).isEqualTo(1);
        Assertions.assertThat(saveArticle.getArticleMenuList().get(0)).isEqualTo(articleMenu1);
        Assertions.assertThat(saveArticle.getArticleMenuList().get(1)).isEqualTo(articleMenu2);
        System.out.println("saveArticle = " + saveArticle.toString());

    }
}
