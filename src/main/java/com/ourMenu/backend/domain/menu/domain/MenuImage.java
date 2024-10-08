package com.ourMenu.backend.domain.menu.domain;

import com.ourMenu.backend.domain.menu.domain.Menu;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuImageId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    //TODO 일부러 EAGER?
    private Menu menu;

    private String url;

    // 연관관계 메서드 //
    public void confirmMenu(Menu menu){
        this.menu = menu;
        menu.addMenuImage(this);
    }
}
