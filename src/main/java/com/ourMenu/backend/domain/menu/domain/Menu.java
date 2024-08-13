package com.ourMenu.backend.domain.menu.domain;

import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.user.domain.User;
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
@Builder(toBuilder = true)

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


    private String memoTitle;

    private String memo;

    private String menuIconType;

    @Builder.Default
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MenuImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MenuTag> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menulist_id")
    private MenuList menuList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    // 단방향?! 양방향?! -> 단방향으로 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    private Long groupId;

//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        this.status = (this.status == null) ? MenuStatus.CREATED : this.status;
//    }

//    @PreUpdate
//    public void preUpdate() {
//        this.modifiedAt = LocalDateTime.now();
//    }

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

    public void removeMenuList(MenuList menuList) {
        if (this.menuList == menuList) {
            this.menuList = null; //
            menuList.removeMenu(this);
        }
    }

    public void removeUser(User user){
        if (this.user == user) {
            this.user = null; //
        }
    }

    public void removePlace(Place place) {
        if (this.place == place) {
            this.place = null;
            place.removeMenu(this);
        }
    }

    public void removeImage(){
        if (images != null) {
            images.clear();
        }
    }


   // 필드 값 변경 함수
   public void changePrice(int price){
       this.price = price;
   }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeMemo(String memo) {
        this.memo = memo;
    }

    public void changeIcon(String icon) {
        this.menuIconType = icon;
    }

    public void updateModifiedAt(){
        this.modifiedAt = LocalDateTime.now();
    }
}
