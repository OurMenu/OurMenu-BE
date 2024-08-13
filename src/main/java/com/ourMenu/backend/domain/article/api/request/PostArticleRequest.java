package com.ourMenu.backend.domain.article.api.request;

import com.ourMenu.backend.domain.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostArticleRequest {

    private String articleTitle;

    private String articleContent;

    private List<ArticleMenuRequest> articleMenus;

    public static Article toEntity(PostArticleRequest postArticleRequest){
        return Article.builder()
                .title(postArticleRequest.articleTitle)
                .content(postArticleRequest.articleContent)
                .build();
    }
}
