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
public class PutArticleRequest {

    private String articleTitle;

    private String articleContent;

    private List<Long> groupIds;

    public static Article toEntity(PutArticleRequest putArticleRequest){
        return Article.builder()
                .title(putArticleRequest.articleTitle)
                .content(putArticleRequest.articleContent)
                .build();
    }
}
