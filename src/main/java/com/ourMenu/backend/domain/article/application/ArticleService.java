package com.ourMenu.backend.domain.article.application;

import com.ourMenu.backend.domain.article.dao.ArticleMenuRepository;
import com.ourMenu.backend.domain.article.dao.ArticleRepository;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleException;
import com.ourMenu.backend.global.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Transactional
    public Article softDelete(Long id){
        Article article = findOne(id);
        article.setStatus(Status.DELETED);
        return article;
    }

    @Transactional
    public void hardDelete(Long id){
        Article article = findOne(id);
        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public Article findOne(Long id){
        return articleRepository.findById(id)
                .orElseThrow(()->new NoSuchArticleException("해당하는 게시물이 없습니다"));
    }
}
