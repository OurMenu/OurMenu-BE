package com.ourMenu.backend.onboarding;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.StoreRequestDTO;
import com.ourMenu.backend.domain.menu.dto.request.TagRequestDto;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.onboarding.application.OnBoardingService;
import com.ourMenu.backend.domain.onboarding.domain.Answer;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import com.ourMenu.backend.domain.onboarding.util.S3Util;
import com.ourMenu.backend.domain.onboarding.util.S3Util;
import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.LoginResponse;
import com.ourMenu.backend.domain.user.application.AccountService;
import com.ourMenu.backend.domain.user.dao.UserDao;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class OnboardingTest {

    @Autowired
    OnBoardingService onBoardingService;

    @Autowired
    MenuListService menuListService;

    @Autowired
    MenuService menuService;

    @Autowired
    AccountService accountService;

    @Autowired
    UserDao userDao;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("questionId와 AnswerType 에 알맞는 단어를 가져온다")
    public void test1(){
        int questionId=1;
        AnswerType answerType=AnswerType.YES;
        List<String> stringList = Question.getAnswerFoodByIdAndAnswerType(questionId, answerType);
        for (String foodName : stringList) {
            System.out.println("foodName = " + foodName);
        }
    }

    @Test
    @DisplayName("regexp를 구성한다.")
    public void test2(){
        int questionId=1;
        AnswerType answerType=AnswerType.YES;
        List<String> stringList = Question.getAnswerFoodByIdAndAnswerType(questionId, answerType);
        String regexp = S3Util.makeRegexp(stringList);
        System.out.println("regexp = " + regexp);
    }
    @Test
    @DisplayName("메뉴판을 저장하고 조회한다.")
    @Transactional
    public void test4() {
        //when
        String menuListTitle="테스트 폴더";
        long userId=initTest3(menuListTitle);

        //given
        MenuList menuList = menuListService.getMenuListByName(menuListTitle, userId);
        //then
        Assertions.assertThat(menuList.getTitle()).isEqualTo(menuListTitle);

    }

    @Test
    @DisplayName("질문에 해당하는 검색 쿼리가 발생한다.(온보딩 과 관련없는 메뉴는 가져오지 않는다")
    @Transactional
    public void test3() {
        //when
        String menuListTitle="테스트 폴더";
        long userId=initTest3(menuListTitle);
        //메뉴를 저장한다.
        StoreRequestDTO storeRequestDTO = StoreRequestDTO.builder()
                .storeName("식당1")
                .storeAddress("식당1주소")
                .storeLatitude(1D)
                .storeLongitude(2D)
                .storeMemo("내용1")
                .build();
        TagRequestDto tagRequestDto= TagRequestDto.builder()
                .tagTitle("태그1")
                .isCustom(false)
                .build();
        PostMenuRequest postMenuRequest1 = PostMenuRequest.builder()
                .menuTitle("메뉴이름")
                .menuPrice(1000)
                .menuIcon("1")
                .menuFolderTitle(menuListTitle)
                .storeInfo(storeRequestDTO)
                .tagInfo(List.of(tagRequestDto))
                .build();

        menuService.createMenu(postMenuRequest1,userId);
        //given
        List<Menu> menuList = onBoardingService.findStoreByQuestionAnswer(userId, 1, AnswerType.YES);
        //then
        Assertions.assertThat(menuList).isEmpty();

    }

    @Test
    @DisplayName("질문에 해당하는 검색 쿼리가 발생한다.(온보딩 과 관련있는 메뉴는 가져온다")
    @Transactional
    public void test5() {
        //when
        String menuListTitle="테스트 폴더";
        long userId=initTest3(menuListTitle);
        //메뉴를 저장한다.
        StoreRequestDTO storeRequestDTO = StoreRequestDTO.builder()
                .storeName("식당1")
                .storeAddress("식당1주소")
                .storeLatitude(1D)
                .storeLongitude(2D)
                .storeMemo("내용1")
                .build();
        TagRequestDto tagRequestDto= TagRequestDto.builder()
                .tagTitle("태그1")
                .isCustom(false)
                .build();
        PostMenuRequest postMenuRequest1 = PostMenuRequest.builder()
                .menuTitle("짬뽕")
                .menuPrice(1000)
                .menuIcon("1")
                .menuFolderTitle(menuListTitle)
                .storeInfo(storeRequestDTO)
                .tagInfo(List.of(tagRequestDto))
                .build();

        menuService.createMenu(postMenuRequest1,userId);
        //given
        //DISLIKE("떡볶이", "닭발", "짬뽕", "닭갈비", "낙곱새", "매운 갈비찜", "갈비찜", "찜갈비"),
        List<Menu> menuList = onBoardingService.findStoreByQuestionAnswer(userId, 1, AnswerType.NO);
        //then
        Assertions.assertThat(menuList.size()).isEqualTo(1);

    }

    @Transactional
    public long initTest3(String menuListTitle) {
        //유저를 저장한다.
        SignUpRequest signUpRequest=new SignUpRequest();
        signUpRequest.setEmail("q1w2e3r4@naver.com");
        signUpRequest.setNickname(menuListTitle);
        signUpRequest.setPassword("q1w2e3r4");
        long userId = userDao.createUser(signUpRequest);

        //메뉴판을 저장한다.
        MenuListRequestDTO menuListRequestDTO = MenuListRequestDTO.builder()
                .menuFolderIcon("1")
                .menuFolderTitle("테스트 폴더")
                .build();

        MenuList menuList = menuListService.createMenuList(menuListRequestDTO,userId);
        return userId;

    }
}
