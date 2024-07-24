package com.ourMenu.backend.ci.menulist.application;

import com.ourMenu.backend.menu.application.MenuService;
import com.ourMenu.backend.menu.dao.MenuRepository;
import com.ourMenu.backend.menu.domain.Menu;
import com.ourMenu.backend.menu.domain.MenuStatus;

import com.ourMenu.backend.menulist.application.MenuListService;
import com.ourMenu.backend.menulist.dao.MenuListRepository;
import com.ourMenu.backend.menulist.domain.MenuList;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MenuListServiceTest {
    @Autowired
    MenuListService menuListService;

    @Autowired
    MenuListRepository menuListRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuService menuService;

    @Autowired
    EntityManager em;

    private MenuList menuList1;
    private MenuList menuList2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        menuList1 = MenuList.builder().title("한강뷰 맛집").build();
        menuList2 = MenuList.builder().title("광진구 맛집").build();

        menu = new Menu();
        menu.setTitle("김치찌개");
        menu.setPrice(10000);
        menu.setStatus(MenuStatus.CREATED);
        menu.setMemo("고기가 많다");
    }

    @Test
    void 모든메뉴판조회() {
        // Given
        menuListService.createMenuList(menuList1);
        menuListService.createMenuList(menuList2);
        em.flush();
        em.clear();

        // When
        List<MenuList> menuLists = menuListService.getAllMenuLists();

        // Then
        assertEquals(2, menuLists.size());
        assertTrue(menuLists.stream().anyMatch(menuList -> menuList.getTitle().equals("한강뷰 맛집")));
        assertTrue(menuLists.stream().anyMatch(menuList -> menuList.getTitle().equals("광진구 맛집")));
    }

    @Test
    void 메뉴판단권조회() {
        // Given
        Long savedId = menuListService.createMenuList(menuList1).getId();
        em.flush();
        em.clear();

        // When
        MenuList foundMenuList = menuListService.getMenuListById(savedId);

        // Then
        assertEquals(menuList1.getTitle(), foundMenuList.getTitle());
    }

    @Test
    void 단권조회_메뉴판이없는경우() {
        // Given
        Long notExistedId = 999L; // 존재하지 않는 ID

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            menuListService.getMenuListById(notExistedId);
        });

        assertEquals("해당하는 메뉴판이 없습니다.", thrown.getMessage());
    }

    @Test
    void 메뉴판추가() {
        // Given
        MenuList newMenuList = MenuList.builder().title("미사 맛집").build();
        
        // When
        MenuList savedMenuList = menuListService.createMenuList(newMenuList);

        em.flush();
        em.clear();
        MenuList foundMenuList = menuListService.getMenuListById(savedMenuList.getId());

        // Then
        assertEquals(foundMenuList.getTitle(), savedMenuList.getTitle());
    }

    @Test
    void 메뉴판_업데이트() {
        // Given
        Long savedId = menuListService.createMenuList(menuList1).getId();
        MenuList updatedMenuList = MenuList.builder().id(savedId).title("건대 맛집").build();

        // When
        menuListService.updateMenuList(savedId, updatedMenuList);
        em.flush();
        em.clear();
        MenuList foundMenuList = menuListService.getMenuListById(savedId);

        // Then
        assertEquals("건대 맛집", foundMenuList.getTitle());
    }

    @Test
    void 메뉴판삭제() {
        // Given
        Long savedId = menuListService.createMenuList(menuList1).getId();
        em.flush();
        em.clear();

        // When
        MenuList deleteMenuList = menuListService.deleteMenuList(savedId);

        // Then
        assertEquals(deleteMenuList.getId(), savedId);
    }

}