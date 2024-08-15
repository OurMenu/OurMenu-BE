package com.ourMenu.backend.domain.article.api.request;

import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PutArticleRequest {

    private String articleTitle;

    private String articleContent;

    private List<ArticleMenuRequest> articleMenus;

    public static Article toEntity(PutArticleRequest putArticleRequest){
        List<ArticleMenu> articleMenuList = putArticleRequest.getArticleMenus().stream().map(ArticleMenuRequest::toEntity).toList();
        return Article.builder()
                .title(putArticleRequest.articleTitle)
                .content(putArticleRequest.articleContent)
                .articleMenuList(articleMenuList)
                .build();
    }
}
