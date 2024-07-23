package com.ourMenu.backend.menu.application;

import com.ourMenu.backend.menu.dao.MenuRepository;
import com.ourMenu.backend.menu.domain.Menu;
import com.ourMenu.backend.menu.domain.MenuStatus;
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
        menu1.setImage("image_url");
        menu1.setStatus(MenuStatus.CREATED);
        menu1.setMemo("고기가 많다");

        menu2 = new Menu();
        menu2.setTitle("짜장면");
        menu2.setPrice(7000);
        menu2.setImage("text_url");
        menu2.setStatus(MenuStatus.CREATED);
        menu2.setMemo("완전 맛있다");
    }

    @Test
    void 모든메뉴조회() {
        // Given
        menuService.createMenu(menu1);
        menuService.createMenu(menu2);


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
        Long nonExistentId = 999L; // 존재하지 않는 ID

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            menuService.getMenuById(nonExistentId);
        });


    }

    @Test
    void createMenu() {

    }

    @Test
    void updateMenu() {
    }

    @Test
    void deleteMenu() {
    }
}