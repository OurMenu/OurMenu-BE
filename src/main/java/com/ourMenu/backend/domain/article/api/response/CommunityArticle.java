package com.ourMenu.backend.domain.article.api.response;

import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommunityArticle {

    private Long articleId;
    private String articleTitle;
    private String articleContent;
    private String userNickname;
    private String userImgUrl;
    private LocalDateTime createdBy;
    private int menusCount;
    private int articleViews;
    private String articleThumbnail;

    public static CommunityArticle toDto(Article article){
        String menuImg = null;
        for (ArticleMenu articleMenu : article.getArticleMenuList()) {
            if (articleMenu.getImgUrl() != null) {
                menuImg = articleMenu.getImgUrl();
                break;
            }
        }
        return CommunityArticle.builder()
                .articleId(article.getId())
                .articleTitle(article.getTitle())
                .articleContent(article.getContent())
                .userNickname(article.getUser().getNickname())
                .userImgUrl(article.getUser().getImgUrl())
                .createdBy(article.getCreatedAt())
                .menusCount(article.getMenuCount())
                .articleViews(article.getViews())
                .articleThumbnail(menuImg)
                .build();
    }
}
