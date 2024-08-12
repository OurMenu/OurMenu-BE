package com.ourMenu.backend.domain.menu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "tag")
    private List<MenuTag> menuTags = new ArrayList<>();

    private Boolean isCustom = true;


    // 중간 테이블 추가
    public void addMenuTag(MenuTag menuTag) {
        menuTags.add(menuTag);
    }

    // 중간 테이블 삭제
    public void removeMenuTag(MenuTag menuTag) {menuTags.remove(menuTag);}
}
