package com.ourMenu.backend.ci.menu.application;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.MenuStatus;
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
class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    EntityManager em;

    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        menu1 = new Menu();
        menu1.setTitle("김치찌개");
        menu1.setPrice(10000);
        menu1.setImgUrl("image_url");
        menu1.setStatus(MenuStatus.CREATED);
        menu1.setMemo("고기가 많다");

        menu2 = new Menu();
        menu2.setTitle("짜장면");
        menu2.setPrice(7000);
        menu2.setImgUrl("text_url");
        menu2.setStatus(MenuStatus.CREATED);
        menu2.setMemo("완전 맛있다");
    }

    @Test
    void 모든메뉴조회() {
        // Given
        menuService.createMenu(menu1);
        menuService.createMenu(menu2);
        em.flush();
        em.clear();

        // When
        List<Menu> menus = menuService.getAllMenus();

        // Then
        assertEquals(2, menus.size());
        assertTrue(menus.stream().anyMatch(menu -> menu.getTitle().equals("김치찌개")));
        assertTrue(menus.stream().anyMatch(menu -> menu.getTitle().equals("짜장면")));

    }

    @Test
    void 메뉴단권조회() {
        // Given
        em.flush();
        em.clear();

        // When
        Long savedId = menuService.createMenu(menu1).getId();
        Menu foundMenu = menuService.getMenuById(savedId);

        // Then
        Menu findMenu = em.find(Menu.class, savedId);
        assertEquals(menu1.getTitle(), findMenu.getTitle());
        assertEquals("김치찌개", findMenu.getTitle());
    }

    @Test
    void 단권조회_메뉴가없는경우() {
        // Given
        Long notExistedId = 999L; // 존재하지 않는 ID

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            menuService.getMenuById(notExistedId);
        });


    }

    @Test
    void 메뉴추가() {
        // Given
        Menu newMenu = new Menu();
        newMenu.setTitle("된장찌개");
        newMenu.setPrice(9000);
        newMenu.setImgUrl("image_url");
        newMenu.setStatus(MenuStatus.CREATED);
        newMenu.setMemo("구수한 맛");

        // When
        Menu savedMenu = menuService.createMenu(newMenu);
        em.flush();
        em.clear();
        Menu findMenu = menuService.getMenuById(savedMenu.getId());


        // Then

        assertEquals(findMenu.getTitle(), savedMenu.getTitle());
        assertEquals(findMenu.getPrice(), savedMenu.getPrice());
    }


    @Test
    void 메뉴업데이트() {
        // Given
        Long savedId = menuService.createMenu(menu1).getId();
        Menu updatedMenu = new Menu();
        updatedMenu.setId(savedId);
        updatedMenu.setTitle("김치찌개");
        updatedMenu.setPrice(12000);
        updatedMenu.setImgUrl("updated_image_url");
        updatedMenu.setStatus(MenuStatus.UPDATED); // 예시로 상태 변경
        updatedMenu.setMemo("라면사리 공짜");

        // When
        menuService.updateMenu(savedId, updatedMenu);
        em.flush();
        em.clear();
        Menu foundMenu = menuService.getMenuById(savedId);

        // Then
        assertEquals(12000, foundMenu.getPrice());
    }

    @Test
    void 메뉴삭제() {

        // Given
        menuService.createMenu(menu1);
        menuService.createMenu(menu2);
        em.flush();
        em.clear();
        Long savedId = menuService.createMenu(menu1).getId();

        // When
        Menu deleteMenu = menuService.deleteMenu(savedId);

        // Then
        assertEquals(deleteMenu.getStatus(), MenuStatus.DELETED);
    }
}