package com.ourMenu.backend.article;

import com.ourMenu.backend.domain.article.api.request.ArticleMenuRequest;
import com.ourMenu.backend.domain.article.api.request.PostArticleRequest;
import com.ourMenu.backend.domain.article.application.ArticleService;
import com.ourMenu.backend.domain.article.dao.ArticleMenuRepository;
import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleException;
import com.ourMenu.backend.domain.article.exception.NoSuchArticleMenuException;
import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.StoreRequestDTO;
import com.ourMenu.backend.domain.menu.dto.request.TagRequestDto;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.application.AccountService;
import com.ourMenu.backend.domain.user.dao.UserDao;
import com.ourMenu.backend.global.common.Status;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@SpringBootTest
@DisplayName("[ArticleServiceTest]")
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;
    @Autowired
    UserDao userDao;
    @Autowired
    MenuService menuService;
    @Autowired
    MenuListService menuListService;
    @Autowired
    EntityManager em;

    ArticleMenu articleMenu1 = ArticleMenu.builder()
            .title("제목1")
            .price(1000)
            .build();
    ArticleMenu articleMenu2 = ArticleMenu.builder()
            .title("제목2")
            .price(2000)
            .build();

    @DisplayName("save 로직에서 발생하는 쿼리수릃 확인한다.")
    @Test
    @Transactional
    public void test1() {
        //given
        List<ArticleMenu> articleMenuList = List.of(articleMenu1, articleMenu2);
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .articleMenuList(articleMenuList)
                .build();
        //when
        Article saveArticle = articleService.save(article);
        //then
        Assertions.assertThat(saveArticle.getId()).isNotNull();
        Assertions.assertThat(saveArticle.getArticleMenuList().get(0)).isEqualTo(articleMenu1);
        Assertions.assertThat(saveArticle.getArticleMenuList().get(1)).isEqualTo(articleMenu2);
        System.out.println("saveArticle = " + saveArticle.toString());

    }

    @DisplayName("soft 삭제를 할 수 있다")
    @Test
    @Transactional
    public void test2() {
        //given
        List<ArticleMenu> articleMenuList = List.of(articleMenu1, articleMenu2);
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .articleMenuList(articleMenuList)
                .build();
        Article saveArticle = articleService.save(article);
        //when
        Article softDeleteArticle = articleService.softDelete(saveArticle.getId());
        //then
        Assertions.assertThat(softDeleteArticle.getStatus()).isEqualTo(Status.DELETED);

    }

    @DisplayName("soft 삭제시 요청한 id의 게시글이 없다면 exception이 발생한다.")
    @Test
    @Transactional
    public void test3() {
        //given

        //when
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchArticleException.class, () -> {
            Article softDeleteArticle = articleService.softDelete(1L);
            Assertions.assertThat(softDeleteArticle.getStatus()).isEqualTo(Status.DELETED);
        });
        //then
        String expectedMessage = "해당하는 게시물이 없습니다";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @DisplayName("hard 삭제를 할 수 있다")
    @Test
    @Transactional
    public void test4() {
        //given
        List<ArticleMenu> articleMenuList = List.of(articleMenu1, articleMenu2);
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .articleMenuList(articleMenuList)
                .build();
        Article saveArticle = articleService.save(article);

        //when
        articleService.hardDelete(saveArticle.getId());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchArticleException.class, () -> {
            Article softDeleteArticle = articleService.softDelete(1L);
            Assertions.assertThat(softDeleteArticle.getStatus()).isEqualTo(Status.DELETED);
        });

        //then
        String expectedMessage = "해당하는 게시물이 없습니다";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(expectedMessage).isEqualTo(actualMessage);

    }

    @DisplayName("hard 삭제시 게시글과 게시글 메뉴 모두 삭제되어야한다")
    @Test
    @Transactional
    public void test5() {
        //given
        List<ArticleMenu> articleMenuList = List.of(articleMenu1, articleMenu2);
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .articleMenuList(articleMenuList)
                .build();
        Article saveArticle = articleService.save(article);

        //when
        articleService.hardDelete(saveArticle.getId());
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchArticleMenuException.class, () -> {
            Long id = article.getArticleMenuList().get(0).getId();
            articleService.findArticleMenu(id);

        });

        //then
        String expectedMessage = "해당하는 게시물메뉴가 없습니다";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(expectedMessage).isEqualTo(actualMessage);

    }

    @DisplayName("게시글을 조회할때 쿼리 수를 최대한 줄인다")
    @Test
    @Transactional
    public void test6() {
        //given
        List<ArticleMenu> articleMenuList = List.of(articleMenu1, articleMenu2);
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .articleMenuList(articleMenuList)
                .build();
        Article saveArticle = articleService.save(article);
        //when
        Article findArticle = articleService.findOne(saveArticle.getId());
        //then
        Assertions.assertThat(findArticle).isEqualTo(saveArticle);

    }

    @DisplayName("saveArticleWithMenu 메서드에서 발생하는 쿼리수릃 확인한다.")
    @Test
    @Transactional
    public void test7() {
        //given
        //유저를 저장한다.
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("q1w2e3r4@naver.com");
        signUpRequest.setNickname("유저1");
        signUpRequest.setPassword("q1w2e3r4");
        long userId = userDao.createUser(signUpRequest);

        //메뉴판을 저장한다.
        String menuListTitle="테스트 메뉴 폴더";
        MenuListRequestDTO menuListRequestDTO1 = MenuListRequestDTO.builder()
                .menuFolderIcon("1")
                .menuFolderTitle(menuListTitle)
                .build();
        MenuList menuList = menuListService.createMenuList(menuListRequestDTO1, userId);

        //가게 정보를 구성한다.
        StoreRequestDTO storeRequestDTO = StoreRequestDTO.builder()
                .storeName("가게1")
                .storeLatitude(111.1D)
                .storeLongitude(111.1D)
                .storeMemo("가게메모")
                .storeAddress("가게주소")
                .build();
        //가게의 태그 리스트를 저장한다
        TagRequestDto tagRequestDto1 = TagRequestDto.builder()
                .tagTitle("태그1")
                .isCustom(true)
                .build();
        TagRequestDto tagRequestDto2 = TagRequestDto.builder()
                .tagTitle("태그2")
                .isCustom(false)
                .build();
        List<TagRequestDto> tagRequestDtoList = List.of(tagRequestDto1, tagRequestDto2);

        //메뉴 저장 request 구성
        PostMenuRequest postMenuRequest = PostMenuRequest.builder()
                .menuTitle("메뉴1")
                .menuPrice(1000)
                .menuMemo("메모")
                .menuIconType("1")
                .menuFolderIds(List.of(menuList.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(tagRequestDtoList)
                .build();

        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, userId);



        //when
        ArticleMenuRequest articleMenuRequest1=ArticleMenuRequest.builder()
                .menuAddress("주소1")
                .menuImgUrl("url1")
                .menuPrice(1000)
                .menuTitle("메뉴이름1")
                .placeTitle("장소1")
                .build();
        //의도적으로 articleMenuRequest2는 이미지가 없다.
        ArticleMenuRequest articleMenuRequest2=ArticleMenuRequest.builder()
                .menuAddress("주소2")
                .menuPrice(1000)
                .menuTitle("메뉴이름1")
                .placeTitle("장소1")
                .build();

        PostArticleRequest postArticleRequest = PostArticleRequest.builder()
                .articleTitle("게시글 제목")
                .articleContent("게시글 내용")
                .articleMenus(List.of(articleMenuRequest1,articleMenuRequest2))
                .build();
        Article article = PostArticleRequest.toEntity(postArticleRequest);
        Article saveArticle = articleService.saveArticleWithMenu(article,userId);
        //then
        System.out.println("saveArticle.toString() = " + saveArticle.toString());

    }
}
