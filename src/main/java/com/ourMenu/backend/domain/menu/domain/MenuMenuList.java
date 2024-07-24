package com.ourMenu.backend.domain.menu.domain;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "menu_menulist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuMenuList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menulist_id")
    private MenuList menuList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public MenuMenuList(Menu menu) {
        this.menu = menu;
    }

    //** 생성 메서드 **//
    public static MenuMenuList createMenuMenuList(Menu menu) {
        return new MenuMenuList(menu);
    }

    //** 연관관계 메서드 **//
    public void addMenu(Menu menu){
        this.setMenu(menu);
        menu.getMenuMenuLists().add(this);
    }
}
