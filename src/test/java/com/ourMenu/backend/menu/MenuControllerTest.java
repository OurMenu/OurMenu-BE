package com.ourMenu.backend.menu;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.StoreRequestDTO;
import com.ourMenu.backend.domain.menu.dto.request.TagRequestDto;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.onboarding.application.OnBoardingService;
import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
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
public class MenuControllerTest {

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

//    @Test
//    @DisplayName("메뉴를 등록한다")
//    @Transactional
//    public void test1() {
//        //when
//        String menuListTitle1 = "테스트 폴더1";
//        String menuListTitle2 = "테스트 폴더2";
//        long userId = initTest3(menuListTitle1, menuListTitle2);
//        MenuList menuList1 = menuListService.getMenuListByName(menuListTitle1, userId);
//        MenuList menuList2 = menuListService.getMenuListByName(menuListTitle2, userId);
//
//        //given
//
//        //가게 정보를 구성한다.
//        StoreRequestDTO storeRequestDTO = StoreRequestDTO.builder()
//                .storeName("가게1")
//                .storeLatitude(111.1D)
//                .storeLongitude(111.1D)
//                .storeMemo("가게메모")
//                .storeAddress("가게주소")
//                .build();
//        //가게의 태그 리스트를 저장한다
//        TagRequestDto tagRequestDto1 = TagRequestDto.builder()
//                .tagTitle("태그1")
//                .isCustom(true)
//                .build();
//        TagRequestDto tagRequestDto2 = TagRequestDto.builder()
//                .tagTitle("태그2")
//                .isCustom(false)
//                .build();
//        List<TagRequestDto> tagRequestDtoList = List.of(tagRequestDto1, tagRequestDto2);
//
//        //메뉴 저장 request 구성
//        PostMenuRequest postMenuRequest = PostMenuRequest.builder()
//                .menuTitle("메뉴1")
//                .menuPrice(1000)
//                .menuMemo("메모")
//                .menuIconType("1")
//                .menuFolderIds(List.of(menuList1.getId()))
//                .storeInfo(storeRequestDTO)
//                .tagInfo(tagRequestDtoList)
//                .build();
//
//        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, userId);
//
//
//        //then
//        Assertions.assertThat(postMenuResponse.getMenuGroupId()).isEqualTo(1);
//
//    }
//    @Test
//    @DisplayName("메뉴를 등록한다(메뉴판 2개이상 공유하는 상태)")
//    @Transactional
//    public void test2() {
//        //when
//        String menuListTitle1 = "테스트 폴더1";
//        String menuListTitle2 = "테스트 폴더2";
//        long userId = initTest3(menuListTitle1, menuListTitle2);
//        MenuList menuList1 = menuListService.getMenuListByName(menuListTitle1, userId);
//        MenuList menuList2 = menuListService.getMenuListByName(menuListTitle2, userId);
//
//        //given
//
//        //가게 정보를 구성한다.
//        StoreRequestDTO storeRequestDTO = StoreRequestDTO.builder()
//                .storeName("가게1")
//                .storeLatitude(111.1D)
//                .storeLongitude(111.1D)
//                .storeMemo("가게메모")
//                .storeAddress("가게주소")
//                .build();
//        //가게의 태그 리스트를 저장한다
//        TagRequestDto tagRequestDto1 = TagRequestDto.builder()
//                .tagTitle("태그1")
//                .isCustom(true)
//                .build();
//        TagRequestDto tagRequestDto2 = TagRequestDto.builder()
//                .tagTitle("태그2")
//                .isCustom(false)
//                .build();
//        List<TagRequestDto> tagRequestDtoList = List.of(tagRequestDto1, tagRequestDto2);
//
//        //메뉴 저장 request 구성
//        PostMenuRequest postMenuRequest = PostMenuRequest.builder()
//                .menuTitle("메뉴1")
//                .menuPrice(1000)
//                .menuMemo("메모")
//                .menuIconType("1")
//                .menuFolderIds(List.of(menuList1.getId(),menuList2.getId()))
//                .storeInfo(storeRequestDTO)
//                .tagInfo(tagRequestDtoList)
//                .build();
//
//        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, userId);
//
//
//        //then
//        Assertions.assertThat(postMenuResponse.getMenuGroupId()).isEqualTo(1);
//
//    }
//
//
//    @Transactional
//    public long initTest3(String menuListTitle1, String menuListTitle2) {
//        //유저를 저장한다.
//        SignUpRequest signUpRequest = new SignUpRequest();
//        signUpRequest.setEmail("q1w2e3r4@naver.com");
//        signUpRequest.setNickname("유저1");
//        signUpRequest.setPassword("q1w2e3r4");
//        long userId = userDao.createUser(signUpRequest);
//
//        //메뉴판을 저장한다.
//        MenuListRequestDTO menuListRequestDTO1 = MenuListRequestDTO.builder()
//                .menuFolderIcon("1")
//                .menuFolderTitle(menuListTitle1)
//                .build();
//
//        MenuListRequestDTO menuListRequestDTO2 = MenuListRequestDTO.builder()
//                .menuFolderIcon("2")
//                .menuFolderTitle(menuListTitle2)
//                .build();
//
//        MenuList menuList1 = menuListService.createMenuList(menuListRequestDTO1, userId);
//        MenuList menuList2 = menuListService.createMenuList(menuListRequestDTO2, userId);
//        return userId;
//
//    }

}
