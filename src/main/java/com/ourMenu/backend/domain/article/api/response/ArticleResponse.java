package com.ourMenu.backend.domain.article.api.response;

import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArticleResponse {

    private Long articleId;
    private String articleTitle;
    private String userNickname;
    private String userImgUrl;
    private LocalDateTime createdBy;
    private String articleContent;
    private String articleThumbnail;
    private int articleViews;
    List<ArticleMenuResponse> articleMenus;

    public static ArticleResponse toDto(Article article, String userImgUrl) {
        String menuImg = null;
        for (ArticleMenu articleMenu : article.getArticleMenuList()) {
            if (articleMenu.getImgUrl() != null) {
                menuImg = articleMenu.getImgUrl();
                break;
            }
        }
        return ArticleResponse.builder()
                .articleId(article.getId())
                .articleTitle(article.getTitle())
                .userNickname(article.getUser().getNickname())
                .userImgUrl(userImgUrl)
                .createdBy(article.getCreatedAt())
                .articleContent(article.getContent())
                .articleThumbnail(menuImg)
                .articleViews(article.getViews())
                .articleMenus(article.getArticleMenuList().stream().map(ArticleMenuResponse::toDto).toList())
                .build();
    }

    public static ArticleResponse toDto(Article article) {
        String menuImg = null;
        for (ArticleMenu articleMenu : article.getArticleMenuList()) {
            if (articleMenu.getImgUrl() != null) {
                menuImg = articleMenu.getImgUrl();
                break;
            }
        }
        return ArticleResponse.builder()
                .articleId(article.getId())
                .articleTitle(article.getTitle())
                .userNickname(article.getUser().getNickname())
                .createdBy(article.getCreatedAt())
                .articleContent(article.getContent())
                .articleThumbnail(menuImg)
                .articleViews(article.getViews())
                .articleMenus(article.getArticleMenuList().stream().map(ArticleMenuResponse::toDto).toList())
                .build();
    }
}
