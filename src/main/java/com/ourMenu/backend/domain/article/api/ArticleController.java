package com.ourMenu.backend.domain.article.api;

import com.ourMenu.backend.domain.article.api.request.PostArticleRequest;
import com.ourMenu.backend.domain.article.api.request.PutArticleRequest;
import com.ourMenu.backend.domain.article.api.response.ArticleResponse;
import com.ourMenu.backend.domain.article.application.ArticleService;
import com.ourMenu.backend.domain.article.domain.Article;
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
        List<Long> menuGroupIds = postArticleRequest.getGroupIds();
        Article saveArticle = articleService.saveArticleWithMenu(article, menuGroupIds, userId);
        return ApiUtils.success(ArticleResponse.toDto(saveArticle));
    }

    @GetMapping("/article/{articleId}")
    public ApiResponse<ArticleResponse> getArticle(@PathVariable Long articleId) {
        Article article = articleService.visitArticleById(articleId);
        return ApiUtils.success(ArticleResponse.toDto(article));
    }

    @PutMapping("/article/{articleId")
    public ApiResponse<ArticleResponse> putArticle(@PathVariable Long articleId, @RequestBody PutArticleRequest putArticleRequest, @UserId Long userId) {
        Article article = PutArticleRequest.toEntity(putArticleRequest);
        List<Long> menuGroupIds = putArticleRequest.getGroupIds();
        Article saveArticle = articleService.updateArticleWithMenu(articleId, article, menuGroupIds, userId);
        return ApiUtils.success(ArticleResponse.toDto(saveArticle));
    }


}
