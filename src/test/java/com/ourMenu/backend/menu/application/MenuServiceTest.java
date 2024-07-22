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

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        menu.setTitle("김치찌개");
        menu.setPrice(10000);
        menu.setImage("image_url");
        menu.setStatus(MenuStatus.CREATED);
        menu.setMemo("고기가 많다");
    }

    @Test
    void getAllMenus() {
    }

    @Test
    void getMenuById() {
        // Given
        Long savedId = menuService.createMenu(menu).getId();
        em.flush();
        // When
        Menu foundMenu = menuService.getMenuById(savedId);

        // Then
        Menu findMenu = em.find(Menu.class, savedId);
        assertEquals(menu.getTitle(), findMenu.getTitle());
        assertEquals("김치찌개", findMenu.getTitle());
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