package com.ourMenu.backend.domain.article.application;

import com.ourMenu.backend.domain.article.api.request.DownloadArticleMenu;
import com.ourMenu.backend.domain.article.api.request.PostArticleRequest;
import com.ourMenu.backend.domain.article.dao.ArticleMenuRepository;
import com.ourMenu.backend.domain.article.dao.ArticleRepository;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import com.ourMenu.backend.domain.article.domain.ORDER_CRITERIA;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleException;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleMenuException;
import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.StoreRequestDTO;
import com.ourMenu.backend.domain.menu.dto.response.MenuDetailDto;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.domain.user.domain.User;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.Status;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMenuRepository articleMenuRepository;
    private final UserService userService;
    private final ArticleMenuService articleMenuService;
    private final MenuService menuService;
    private final MenuListService menuListService;

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
        article.getArticleMenuList().forEach(articleMenu -> {
            articleMenu.deleteArticle();
            articleMenuRepository.delete(articleMenu);
        });
        articleRepository.delete(article);
    }

    @Transactional
    public void hardDeleteByUserId(Long id, Long userId) {
        Article article = findOne(id);
        if(!article.getUser().getId().equals(userId)){
            throw new RuntimeException("권한이 없습니다");
        }
        article.getArticleMenuList().forEach(articleMenu -> {
            articleMenu.deleteArticle();
            articleMenuRepository.delete(articleMenu);
        });
        articleRepository.delete(article);
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
    public Article visitArticleById(Long id) {
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
    public Article saveArticleWithMenu(Article article, Long userId) {
        User user = userService.getUserById(userId).get();
        article.confirmUser(user);
        for (ArticleMenu articleMenu : article.getArticleMenuList()) {
            articleMenu.confirmArticle(article);
            articleMenuService.save(articleMenu);
        }
        return save(article);
    }

    @Transactional
    public Article updateArticleWithMenu(Long articleId, Article article, Long userId) {

        Article findArticle = findOne(articleId);
        User user = findArticle.getUser();
        if (!user.getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다");
        }

        findArticle.getArticleMenuList().forEach(articleMenu -> {
            articleMenu.deleteArticle();
            articleMenuRepository.delete(articleMenu);
        });

        findArticle.deleteAllArticleMenus();

        for (ArticleMenu articleMenu : article.getArticleMenuList()) {
            findArticle.addArticleMenu(articleMenu);
            articleMenu.confirmArticle(findArticle);
            articleMenuService.save(articleMenu);
        }
        findArticle.update(article);
        return findArticle;
    }

    /**
     * 조건에 맞는 게시글을 조회한다
     *
     * @param title         검색어
     * @param page          페이지
     * @param size          페이지 크기
     * @param orderCriteria 정렬 기준
     * @param isMyArticle   본인의 게시물만 가져올 것인지
     * @return 조회된 게시글들
     */
    @Transactional
    public List<Article> findArticleByUserIdAndOrderAndPage(String title, int page, int size, ORDER_CRITERIA orderCriteria, Boolean isMyArticle, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderCriteria.getDirection(), orderCriteria.getProperty()));
        Page<Article> menuPage;
        if (isMyArticle) {
            menuPage = articleRepository.findAllByUserAndTitleContaining(title, pageable, userId);
            return menuPage.getContent();
        }

        menuPage = articleRepository.findAllByTitleContaining(title, pageable);
        return menuPage.getContent();
    }


    private final S3Client s3Client;
    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    public String getUserImgUrl(String img_url) {
        String fileUrl = "";
        if (fileUrl != null && !img_url.isBlank()) {
            fileUrl = s3Client.utilities()
                    .getUrl(builder -> builder.bucket(bucketName).key(img_url))
                    .toExternalForm();
        }
        return fileUrl;
    }

    @Transactional
    public ArticleMenu addSharedCount(Long articleMenuId) {
        ArticleMenu articleMenu = findArticleMenu(articleMenuId);
        articleMenu.addSharedCount();
        return articleMenu;
    }

    @Transactional
    public Long downloadMenus(Long articleMenuId, DownloadArticleMenu downloadArticleMenu, Long userId) {
        ArticleMenu articleMenu = findArticleMenu(articleMenuId);
        articleMenu.addSharedCount();
        List<Long> menuFolderIds = downloadArticleMenu.getArticleMenuIds();
        StoreRequestDTO storeRequestDTO = StoreRequestDTO.builder()
                .storeName(articleMenu.getPlaceTitle())
                .storeMemo(articleMenu.getPlaceMemo())
                .storeAddress(articleMenu.getAddress())
                .storeLatitude(articleMenu.getPlaceLatitude())
                .storeLongitude(articleMenu.getPlaceLongitude())
                .build();

        PostMenuRequest postMenuRequest = PostMenuRequest.builder()
                .menuTitle(articleMenu.getMenuMemoTitle())
                .menuPrice(articleMenu.getPrice())
                .menuMemo(articleMenu.getPlaceMemo())
                .menuMemoTitle(articleMenu.getMenuMemoTitle())
                .menuIconType(articleMenu.getMenuIconType())
                .storeInfo(storeRequestDTO)
                .tagInfo(Collections.emptyList())
                .menuFolderIds(menuFolderIds)
                .build();
        return menuService.createMenu(postMenuRequest, userId).getMenuGroupId();
    }

    @Transactional
    public Article getArticleByGroupId(PostArticleRequest postArticleRequest, Long userId) {
        List<Menu> menuList = menuService.getMenuByGroupId(postArticleRequest.getGroupIds(),userId);
        Article article = Article.builder()
                .user(userService.getUserById(userId).get())
                .title(postArticleRequest.getArticleTitle())
                .content(postArticleRequest.getArticleContent())
                .build();
        Article saveArticle = save(article);

        for (Menu menu : menuList) {
            ArticleMenu articleMenu = ArticleMenu.builder()
                    .article(saveArticle)
                    .title(menu.getTitle())
                    .price(menu.getPrice())
                    .placeTitle(menu.getPlace().getTitle())
                    .address(menu.getPlace().getAddress())
                    .imgUrl(menu.getImages().size() == 0  ? null:menu.getImages().get(0).getUrl())
                    .groupId(menu.getGroupId())
                    .menuMemoTitle(menu.getMemoTitle())
                    .menuIconType(menu.getMenuIconType())
                    .placeMemo(menu.getPlace().getInfo())
                    .placeLatitude(menu.getPlace().getLatitude())
                    .placeLongitude(menu.getPlace().getLongitude())
                    .build();
            ArticleMenu saveArticleMenu = articleMenuService.save(articleMenu);
            saveArticle.addArticleMenu(saveArticleMenu);
        }
        return saveArticle;
    }

}
