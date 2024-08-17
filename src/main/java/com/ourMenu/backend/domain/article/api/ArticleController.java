package com.ourMenu.backend.domain.article.api;

import com.ourMenu.backend.domain.article.api.request.DownloadArticleMenu;
import com.ourMenu.backend.domain.article.api.request.PostArticleRequest;
import com.ourMenu.backend.domain.article.api.request.PutArticleRequest;
import com.ourMenu.backend.domain.article.api.response.ArticleMenuResponse;
import com.ourMenu.backend.domain.article.api.response.ArticleResponse;
import com.ourMenu.backend.domain.article.api.response.CommunityArticle;
import com.ourMenu.backend.domain.article.application.ArticleService;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import com.ourMenu.backend.domain.article.domain.ORDER_CRITERIA;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ApiResponse<ArticleResponse> postArticle(@RequestBody PostArticleRequest postArticleRequest, @UserId Long userId) {
        Article article = PostArticleRequest.toEntity(postArticleRequest);
        Article saveArticle = articleService.saveArticleWithMenu(article, userId);
        if (saveArticle.getUser().getImgUrl() == null) {
            return ApiUtils.success(ArticleResponse.toDto(article));
        }
        String userImgUrl = articleService.getUserImgUrl(article.getUser().getImgUrl());
        return ApiUtils.success(ArticleResponse.toDto(saveArticle, userImgUrl));
    }

    @GetMapping("/article/{articleId}")
    public ApiResponse<ArticleResponse> getArticle(@PathVariable Long articleId) {
        Article article = articleService.visitArticleById(articleId);
        if (article.getUser().getImgUrl() == null) {
            return ApiUtils.success(ArticleResponse.toDto(article));
        }
        String userImgUrl = articleService.getUserImgUrl(article.getUser().getImgUrl());
        return ApiUtils.success(ArticleResponse.toDto(article, userImgUrl));
    }

    @PutMapping("/article/{articleId}")
    public ApiResponse<ArticleResponse> putArticle(@PathVariable Long articleId, @RequestBody PutArticleRequest putArticleRequest, @UserId Long userId) {
        Article article = PutArticleRequest.toEntity(putArticleRequest);
        Article saveArticle = articleService.updateArticleWithMenu(articleId, article, userId);
        if (saveArticle.getUser().getImgUrl() == null) {
            return ApiUtils.success(ArticleResponse.toDto(saveArticle));
        }
        String userImgUrl = articleService.getUserImgUrl(saveArticle.getUser().getImgUrl());
        return ApiUtils.success(ArticleResponse.toDto(saveArticle, userImgUrl));
    }

    @GetMapping("")
    public ApiResponse<List<CommunityArticle>> getArticleList(@RequestParam(required = false) String title,//검색어
                                                              @RequestParam(defaultValue = "0") int page, // 페이지 번호, 기본값은 0
                                                              @RequestParam(defaultValue = "5") int size, // 페이지 크기, 기본값은 5
                                                              @RequestParam(value = "orderCriteria", defaultValue = "CREATED_AT_DESC") ORDER_CRITERIA orderCriteria,
                                                              @RequestParam(defaultValue = "false") Boolean isMyArticle,
                                                              @UserId Long userId) {
        List<Article> articleList = articleService.findArticleByUserIdAndOrderAndPage(title, page, size, orderCriteria, isMyArticle, userId);
        List<CommunityArticle> communityArticleList = articleList.stream().map(article -> {
            if (article.getUser().getImgUrl() == null) {
                return CommunityArticle.toDto(article);
            }
            String userImgUrl = articleService.getUserImgUrl(article.getUser().getImgUrl());
            return CommunityArticle.toDto(article, userImgUrl);
        }).toList();
        return ApiUtils.success(communityArticleList);
    }

    @GetMapping("/article/menu/{articleMenuId}")
    public ApiResponse<ArticleMenuResponse> getSharedArticleMenu(@PathVariable Long articleMenuId){
        ArticleMenu articleMenu = articleService.addSharedCount(articleMenuId);

        return ApiUtils.success(ArticleMenuResponse.toDto(articleMenu));
    }

    @PostMapping("/article/menu/{articleMenuId}")
    public ApiResponse<String> downloadArticleMenu(@PathVariable Long articleMenuId,
                                                                @RequestBody DownloadArticleMenu downloadArticleMenu,
                                                                @UserId Long userId){
        articleService.downloadMenus(articleMenuId, downloadArticleMenu, userId);

        return ApiUtils.success("success");
    }
}
