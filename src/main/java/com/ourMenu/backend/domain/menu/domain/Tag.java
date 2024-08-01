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


    public void addMenuTag(MenuTag menuTag) {
        menuTags.add(menuTag);
    }
}
