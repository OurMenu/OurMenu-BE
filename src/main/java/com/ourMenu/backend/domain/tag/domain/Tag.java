package com.ourMenu.backend.domain.tag.domain;

import com.ourMenu.backend.domain.menutag.domain.MenuTag;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "tag")
    private List<MenuTag> menuTags;

    private Boolean isCustom = true;


    public void addMenuTag(MenuTag menuTag) {
        menuTags.add(menuTag);
    }
}
