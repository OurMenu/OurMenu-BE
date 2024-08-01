package com.ourMenu.backend.article;

import com.ourMenu.backend.domain.article.application.ArticleService;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleException;
import com.ourMenu.backend.global.common.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@SpringBootTest
@DisplayName("[ArticleServiceTest]")
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;
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

    @DisplayName("save 로직에서 발생하는 쿼리수릃 확인한다.")
    @Test
    @Transactional
    public void test1() {
        //given
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

    @DisplayName("soft 삭제를 할 수 있다")
    @Test
    @Transactional
    public void test2() {
        //given
        List<ArticleMenu> articleMenuList = List.of(articleMenu1, articleMenu2);
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .articleMenuList(articleMenuList)
                .build();
        //when
        Article saveArticle = articleService.save(article);
        Article softDeleteArticle = articleService.softDelete(saveArticle.getId());
        //then
        Assertions.assertThat(softDeleteArticle.getStatus()).isEqualTo(Status.DELETED);

    }

    @DisplayName("soft 삭제시 요청한 id의 게시글이 없다면 exception이 발생한다.")
    @Test
    @Transactional
    public void test3(){
        //given

        //when
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchArticleException.class, () -> {
            Article softDeleteArticle = articleService.softDelete(1L);
            Assertions.assertThat(softDeleteArticle.getStatus()).isEqualTo(Status.DELETED);
        });
        //then
        String expectedMessage = "해당하는 게시물이 없습니다";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(expectedMessage).isEqualTo(actualMessage);

    }
}
