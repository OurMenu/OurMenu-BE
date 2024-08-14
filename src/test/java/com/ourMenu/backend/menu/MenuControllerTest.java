package com.ourMenu.backend.menu;

import com.ourMenu.backend.domain.menu.dto.response.MenuDto;
import com.ourMenu.backend.domain.menu.dto.response.PostMenuResponse;
import com.ourMenu.backend.domain.menulist.application.MenuListService;
import org.springframework.boot.test.context.SpringBootTest;
import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dto.request.PostMenuRequest;
import com.ourMenu.backend.domain.menu.dto.request.StoreRequestDTO;
import com.ourMenu.backend.domain.menu.dto.request.TagRequestDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
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

    @Test
    @DisplayName("메뉴를 등록한다")

    public void test1() {
        //when
        String menuListTitle1 = "테스트 폴더1";
        String menuListTitle2 = "테스트 폴더2";
        long userId = initTest3(menuListTitle1, menuListTitle2);
        MenuList menuList1 = menuListService.getMenuListByName(menuListTitle1, userId);
        MenuList menuList2 = menuListService.getMenuListByName(menuListTitle2, userId);
        //given
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
                .menuFolderIds(List.of(menuList1.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(tagRequestDtoList)
                .build();
        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, userId);
        //then
        assertThat(postMenuResponse.getMenuGroupId()).isEqualTo(1);
    }

    @Test
    @DisplayName("메뉴를 등록한다(메뉴판 2개이상 공유하는 상태)")

    public void test2() {
        //when
        String menuListTitle1 = "테스트 폴더1";
        String menuListTitle2 = "테스트 폴더2";
        long userId = initTest3(menuListTitle1, menuListTitle2);
        MenuList menuList1 = menuListService.getMenuListByName(menuListTitle1, userId);
        MenuList menuList2 = menuListService.getMenuListByName(menuListTitle2, userId);
        //given
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
                .menuFolderIds(List.of(menuList1.getId(), menuList2.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(tagRequestDtoList)
                .build();
        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, userId);
        //then
        assertThat(postMenuResponse.getMenuGroupId()).isEqualTo(1);
    }

    public long initTest3(String menuListTitle1, String menuListTitle2) {
        //유저를 저장한다.
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("q1w2e3r4@naver.com");
        signUpRequest.setNickname("유저1");
        signUpRequest.setPassword("q1w2e3r4");
        long userId = userDao.createUser(signUpRequest);
        //메뉴판을 저장한다.
        MenuListRequestDTO menuListRequestDTO1 = MenuListRequestDTO.builder()
                .menuFolderIcon("1")
                .menuFolderTitle(menuListTitle1)
                .build();
        MenuListRequestDTO menuListRequestDTO2 = MenuListRequestDTO.builder()
                .menuFolderIcon("2")
                .menuFolderTitle(menuListTitle2)
                .build();
        MenuList menuList1 = menuListService.createMenuList(menuListRequestDTO1, userId);
        MenuList menuList2 = menuListService.createMenuList(menuListRequestDTO2, userId);
        return userId;
    }

    @Test
    @DisplayName("메뉴 태그로 메뉴를 조회한다.(태그가 3개 이상인 상황)")
    public void TagTest() {
        //when
        String menuListTitle1 = "테스트 폴더1";
        String menuListTitle2 = "테스트 폴더2";
        long userId = initTest3(menuListTitle1, menuListTitle2);
        MenuList menuList1 = menuListService.getMenuListByName(menuListTitle1, userId);
        MenuList menuList2 = menuListService.getMenuListByName(menuListTitle2, userId);
        //given
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
        TagRequestDto tagRequestDto3 = TagRequestDto.builder()
                .tagTitle("태그3")
                .isCustom(false)
                .build();

        List<TagRequestDto> tagRequestDtoList = List.of(tagRequestDto1, tagRequestDto2, tagRequestDto3);
        //메뉴 저장 request 구성
        PostMenuRequest postMenuRequest = PostMenuRequest.builder()
                .menuTitle("메뉴1")
                .menuPrice(1000)
                .menuMemo("메모")
                .menuIconType("1")
                .menuFolderIds(List.of(menuList1.getId(), menuList2.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(tagRequestDtoList)
                .build();
        PostMenuResponse postMenuResponse = menuService.createMenu(postMenuRequest, userId);

        String[] tags = {"태그1", "태그2", "태그3"};
        int tagLength = tags.length;
        Pageable pageable = PageRequest.of(0, 100);

        Page<MenuDto> findMenu = menuService.getAllMenusByCriteria2(tags, null, userId, 5000, 50000, pageable);
        //then
        assertThat(postMenuResponse.getMenuGroupId()).isEqualTo(1);
        assertThat(findMenu.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("선택한 태그 정확히 해당하는 메뉴만 조회한다.(태그1,태그2 만 가지는 메뉴 VS 태그1,태그2,태그3 가지는 메뉴 조회 상황)")
    public void preciseTagTest() {
        //when
        String menuListTitle1 = "테스트 폴더1";
        String menuListTitle2 = "테스트 폴더2";
        long userId = initTest3(menuListTitle1, menuListTitle2);
        MenuList menuList1 = menuListService.getMenuListByName(menuListTitle1, userId);
        MenuList menuList2 = menuListService.getMenuListByName(menuListTitle2, userId);
        //given
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
        TagRequestDto tagRequestDto3 = TagRequestDto.builder()
                .tagTitle("태그3")
                .isCustom(false)
                .build();

        List<TagRequestDto> menu1tagRequestDtoList = List.of(tagRequestDto1, tagRequestDto2); // 태그 2개
        List<TagRequestDto> menu2tagRequestDtoList = List.of(tagRequestDto1, tagRequestDto2, tagRequestDto3); // 태그 3개

        //메뉴 저장 request 구성

        PostMenuRequest postMenuRequest = PostMenuRequest.builder()
                .menuTitle("메뉴1")
                .menuPrice(1000)
                .menuMemo("메모")
                .menuIconType("1")
                .menuFolderIds(List.of(menuList1.getId(), menuList2.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(menu1tagRequestDtoList)
                .build();
        menuService.createMenu(postMenuRequest, userId);

        PostMenuRequest postMenuRequest2 = PostMenuRequest.builder()
                .menuTitle("메뉴2")
                .menuPrice(1000)
                .menuMemo("메모")
                .menuIconType("1")
                .menuFolderIds(List.of(menuList1.getId(), menuList2.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(menu2tagRequestDtoList)
                .build();
        menuService.createMenu(postMenuRequest2, userId);

        String[] tags = {"태그1", "태그2", "태그3"};
        int tagLength = tags.length;
        Pageable pageable = PageRequest.of(0, 100);

        Page<MenuDto> findMenu = menuService.getAllMenusByCriteria2(tags, null, userId, 5000, 50000, pageable);
        //then
        assertThat(findMenu.getContent().size()).isEqualTo(1);
        assertThat(findMenu.getContent().get(0).getMenuTitle()).isEqualTo("메뉴2");
    }

    @Test
    @DisplayName("여러 태그 조합으로 메뉴를 조회할 수 있다..")
    public void multipleTagCombinationsTest() {
        //when
        String menuListTitle1 = "테스트 폴더1";
        String menuListTitle2 = "테스트 폴더2";
        long userId = initTest3(menuListTitle1, menuListTitle2);
        MenuList menuList1 = menuListService.getMenuListByName(menuListTitle1, userId);
        MenuList menuList2 = menuListService.getMenuListByName(menuListTitle2, userId);

        //given
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
        TagRequestDto tagRequestDto3 = TagRequestDto.builder()
                .tagTitle("태그3")
                .isCustom(false)
                .build();
        TagRequestDto tagRequestDto4 = TagRequestDto.builder()
                .tagTitle("태그4")
                .isCustom(false)
                .build();

        // 메뉴 저장 request 구성
        PostMenuRequest postMenuRequest1 = PostMenuRequest.builder()
                .menuTitle("메뉴1")
                .menuPrice(1000)
                .menuMemo("메모")
                .menuIconType("1")
                .menuFolderIds(List.of(menuList1.getId(), menuList2.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(List.of(tagRequestDto1, tagRequestDto2)) // 태그 2개
                .build();

        // 1,2

        // 1,2,3

        // 1, 3, 4
        PostMenuRequest postMenuRequest2 = PostMenuRequest.builder()
                .menuTitle("메뉴2")
                .menuPrice(1000)
                .menuMemo("메모")
                .menuIconType("1")
                .menuFolderIds(List.of(menuList1.getId(), menuList2.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(List.of(tagRequestDto1, tagRequestDto2, tagRequestDto3)) // 태그 3개
                .build();

        PostMenuRequest postMenuRequest3 = PostMenuRequest.builder()
                .menuTitle("메뉴3")
                .menuPrice(1000)
                .menuMemo("메모")
                .menuIconType("1")
                .menuFolderIds(List.of(menuList1.getId(), menuList2.getId()))
                .storeInfo(storeRequestDTO)
                .tagInfo(List.of(tagRequestDto1, tagRequestDto3, tagRequestDto4)) // 태그 2개
                .build();

        // 메뉴 저장
        menuService.createMenu(postMenuRequest1, userId);
        menuService.createMenu(postMenuRequest2, userId);
        menuService.createMenu(postMenuRequest3, userId);

        // 다양한 태그 조합으로 조회

        String[] tags = new String[]{"태그1"};

        // 각 조합에 대해 테스트
        Pageable pageable = PageRequest.of(0, 100);

        Page<MenuDto> findMenu = menuService.getAllMenusByCriteria2(tags, null, userId, 0, Integer.MAX_VALUE, pageable);

        String[] tags2 = new String[]{"태그1", "태그2", "태그3"};
        Page<MenuDto> findMenu2 = menuService.getAllMenusByCriteria2(tags2, null, userId, 0, Integer.MAX_VALUE, pageable);
        // then

        assertThat(findMenu.getContent().size()).isEqualTo(3);
        assertThat(findMenu2.getContent().size()).isEqualTo(1);
        // assertThat(findMenu.getContent().get(0).getMenuTitle()).isEqualTo("메뉴1");
    }
}

