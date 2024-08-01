package com.ourMenu.backend.domain.article.application;

import com.ourMenu.backend.domain.article.dao.ArticleMenuRepository;
import com.ourMenu.backend.domain.article.dao.ArticleRepository;
import com.ourMenu.backend.domain.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMenuRepository articleMenuRepository;

    /**
     * 게시글을 저장한다.
     *
     * @param article 메뉴를 포함한 게시글
     * @return 저장된 article 엔티티
     */
    @Transactional
    public Article save(Article article) {
        Article saveArticle = articleRepository.save(article);
        articleMenuRepository.saveAll(article.getArticleMenuList());
        return saveArticle;
    }
}
