package com.ourMenu.backend.domain.menu.domain;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.global.common.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String title;
    private int price;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.CREATED;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    private String memo;
    private String icon;

    @Builder.Default
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuTag> tags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "menulist_id")
    private MenuList menuList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    private Place place;

//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        this.status = (this.status == null) ? MenuStatus.CREATED : this.status;
//    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    public void addMenuImage(MenuImage menuImage) {
        //System.out.println("menuImage = " + menuImage);
         images.add(menuImage);
    }

    public void addMenuTag(MenuTag menuTag) {
        tags.add(menuTag);
    }

    public void addPlace(Place place){
        this.place = place;
    }

    // 연관관계 메서드 //
    public void confirmMenuList(MenuList menuList){
        this.menuList = menuList;
        menuList.addMenu(this);
    }

    public void confirmPlace(Place place){
        this.place = place;
        place.addMenu(this);
    }


}
