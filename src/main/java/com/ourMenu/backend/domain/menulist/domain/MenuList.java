package com.ourMenu.backend.domain.menulist.domain;

import com.ourMenu.backend.domain.menu.domain.Menu;
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
public class MenuList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menulist_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.CREATED;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String title;

    private String iconType;
    private Long priority;

    @Lob
    @Column(name = "image")
    private String imgUrl;

    @Builder.Default
    @OneToMany(mappedBy = "menuList")
    private List<Menu> menus = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = (this.status == null) ? Status.CREATED : this.status;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
    }

    // 연관관계 메서드 //
    public void confirmUser(User user){
        this.user = user;
        user.addMenuList(this);
    }

    public void removeMenu(Menu menu) {
        menus.remove(menu);
    }

    public void softDelete() {
        this.status = Status.DELETED;
        this.priority = null;
    }
}
