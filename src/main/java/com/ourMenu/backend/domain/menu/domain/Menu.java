package com.ourMenu.backend.domain.menu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private MenuStatus status = MenuStatus.CREATED;

    @OneToMany(mappedBy = "menu")
    private List<MenuMenuList> menuMenuLists = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String memo;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = (this.status == null) ? MenuStatus.CREATED : this.status;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

}
