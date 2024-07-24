package com.ourMenu.backend.menulist.domain;

import com.ourMenu.backend.menu.domain.MenuMenuList;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menulist_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MenuListStatus status;

    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String title;

    @Lob
    private String image;


    @OneToMany(mappedBy = "menuList", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MenuMenuList> menuMenuLists = new ArrayList<>();


    //** 연관관계 메서드 **//
    public void addMenuMenuList(MenuMenuList menuMenuList){
        this.menuMenuLists.add(menuMenuList);
        menuMenuList.setMenuList(this);
    }

    public void removeMenuMenuList(MenuMenuList menuMenuList) {
        this.menuMenuLists.remove(menuMenuList);
        menuMenuList.setMenuList(null);
    }

}
