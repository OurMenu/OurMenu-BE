package com.ourMenu.backend.domain.article.api;

import com.ourMenu.backend.domain.article.api.request.PostArticleRequest;
import com.ourMenu.backend.domain.article.application.ArticleService;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.global.argument_resolver.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public String postArticle(@RequestBody PostArticleRequest postArticleRequest, @UserId Long userId) {
        Article article = PostArticleRequest.toEntity(postArticleRequest);
        List<Long> menuGroupIds = postArticleRequest.getGroupIds();
        articleService.saveArticleWithMenu(article,menuGroupIds,userId);
        return "success";
    }

}
