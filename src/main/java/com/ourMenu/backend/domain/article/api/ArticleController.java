package com.ourMenu.backend.domain.article.api;

import com.ourMenu.backend.domain.article.api.request.PostArticleRequest;
import com.ourMenu.backend.domain.article.api.request.PutArticleRequest;
import com.ourMenu.backend.domain.article.api.response.ArticleResponse;
import com.ourMenu.backend.domain.article.api.response.CommunityArticle;
import com.ourMenu.backend.domain.article.application.ArticleService;
import com.ourMenu.backend.domain.article.domain.Article;
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
        Article saveArticle = articleService.saveArticleWithMenu(article,userId);
        return ApiUtils.success(ArticleResponse.toDto(saveArticle));
    }

    @GetMapping("/article/{articleId}")
    public ApiResponse<ArticleResponse> getArticle(@PathVariable Long articleId) {
        Article article = articleService.visitArticleById(articleId);
        return ApiUtils.success(ArticleResponse.toDto(article));
    }

    @PutMapping("/article/{articleId}")
    public ApiResponse<ArticleResponse> putArticle(@PathVariable Long articleId, @RequestBody PutArticleRequest putArticleRequest, @UserId Long userId) {
        Article article = PutArticleRequest.toEntity(putArticleRequest);
        Article saveArticle = articleService.updateArticleWithMenu(articleId, article, userId);
        return ApiUtils.success(ArticleResponse.toDto(saveArticle));
    }

    @GetMapping("/community")
    public ApiResponse<List<CommunityArticle>> getArticleList(@RequestParam(required = false) String title,//검색어
                                 @RequestParam(defaultValue = "0") int page, // 페이지 번호, 기본값은 0
                                 @RequestParam(defaultValue = "5") int size, // 페이지 크기, 기본값은 5
                                 @RequestParam(value = "orderCriteria", defaultValue = "title") ORDER_CRITERIA orderCriteria,
                                 @UserId Long userId){
        List<Article> articleList = articleService.findArticleByUserIdAndOrderAndPage(title, page, size, orderCriteria, userId);
        List<CommunityArticle> communityArticleList = articleList.stream().map(CommunityArticle::toDto).toList();
        return ApiUtils.success(communityArticleList);
    }

}
