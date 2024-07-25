package com.ourMenu.backend.domain.menulist.domain;

import com.ourMenu.backend.domain.menu.domain.MenuMenuList;
import com.ourMenu.backend.domain.menu.domain.MenuStatus;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;
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

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String title;

    @Lob
    @Column(name = "image")
    private String imgUrl;


    @OneToMany(mappedBy = "menuList", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MenuMenuList> menuMenuLists = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = (this.status == null) ? MenuListStatus.CREATED : this.status;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

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
