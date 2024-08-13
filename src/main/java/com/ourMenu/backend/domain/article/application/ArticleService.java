package com.ourMenu.backend.domain.article.application;

import com.ourMenu.backend.domain.article.dao.ArticleMenuRepository;
import com.ourMenu.backend.domain.article.dao.ArticleRepository;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import com.ourMenu.backend.domain.article.domain.ORDER_CRITERIA;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleException;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleMenuException;
import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.response.MenuDetailDto;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.domain.User;
import com.ourMenu.backend.global.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMenuRepository articleMenuRepository;
    private final UserService userService;
    private final MenuService menuService;
    private final ArticleMenuService articleMenuService;

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

    /**
     * softdelete 논리적 삭제
     *
     * @param id 게시글 id
     * @return 논리적으로 삭제된 article (물리적으로 변경된 article)
     */
    @Transactional
    public Article softDelete(Long id) {
        Article article = findOne(id);
        article.setStatus(Status.DELETED);
        return article;
    }

    /**
     * softdelete 물리적 삭제
     *
     * @param id 게시글 id
     */
    @Transactional
    public void hardDelete(Long id) {
        Article article = findOne(id);
        articleRepository.delete(article);
        articleMenuRepository.deleteAll(article.getArticleMenuList());
    }

    /**
     * 게시글 조회
     *
     * @param id
     * @return
     * @throws NoSuchArticleException id 값을 가진 article이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public Article findOne(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(NoSuchArticleException::new);
    }

    @Transactional
    public Article visitArticleById(Long id){
        Article article = findOne(id);
        article.visit();
        return article;
    }

    /**
     * 게시글 메뉴조회 (test를 위해 필요)
     *
     * @param id 게시글 id
     * @return 게시글
     */
    @Transactional(readOnly = true)
    public ArticleMenu findArticleMenu(Long id) {
        return articleMenuRepository.findById(id)
                .orElseThrow(NoSuchArticleMenuException::new);
    }

    /**
     * article을 articleMenu와 함께 저장한다.
     *
     * @param article
     * @return 저장한 article
     */
    @Transactional
    public Article saveArticleWithMenu(Article article) {
        Article saveArticle = save(article);
        for (ArticleMenu articleMenu : article.getArticleMenuList()) {
            articleMenuService.save(articleMenu);
        }
        return saveArticle;
    }

    @Transactional
    public Article updateArticleWithMenu(Long articleId, Article article, Long userId) {

        User user = userService.getUserById(userId).get();
        Article findArticle = findOne(articleId);
        findArticle.deleteAllArticleMenus();
        for (ArticleMenu articleMenu : article.getArticleMenuList()) {
            article.addArticleMenu(articleMenu);
        }
        findArticle.update(article);
        return findArticle;
    }

    /**
     * 조건에 맞는 게시글을 조회한다
     * @param title 검색어
     * @param page 페이지
     * @param size 페이지 크기
     * @param orderCriteria 정렬 기준
     * @param userId 유저 Id
     * @return 조회된 게시글들
     */
    @Transactional
    public List<Article> findArticleByUserIdAndOrderAndPage(String title, int page, int size, ORDER_CRITERIA orderCriteria, Long userId){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderCriteria.getDirection(), orderCriteria.getProperty()));
        Page<Article> menuPage = articleRepository.findAllByUserAndTitleContaining(title, userId, pageable);
        return menuPage.getContent();
    }
}
