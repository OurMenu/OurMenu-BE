package com.ourMenu.backend.domain.menu.domain;

import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.domain.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // 연관관계 메서드 //
    public void confirmTag(Tag tag){
        this.tag = tag;
        tag.addMenuTag(this);
    }

    public void confirmMenu(Menu menu){
        this.menu = menu;
        menu.addMenuTag(this);
    }
}
