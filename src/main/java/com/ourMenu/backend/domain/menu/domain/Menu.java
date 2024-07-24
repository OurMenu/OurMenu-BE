package com.ourMenu.backend.domain.menu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String title;
    private int price;

    @Lob
    @Column(name = "image")
    private String ImgUrl;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    @OneToMany(mappedBy = "menu")
    private List<MenuMenuList> menuMenuLists = new ArrayList<>();

    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String memo;

}
