package com.ourMenu.backend.menu.domain;

import com.ourMenu.backend.menulist.MenuList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Menu_MenuList")
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

    //** 연관관계 메서드 **//
    public void addMenu(Menu menu){
        this.setMenu(menu);
        menu.getMenuMenuLists().add(this);
    }
}
