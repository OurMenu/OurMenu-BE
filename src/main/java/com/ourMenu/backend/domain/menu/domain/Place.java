package com.ourMenu.backend.domain.menu.domain;

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
@Builder
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double latitude;
    private Double longitude;

    private String title;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.CREATED;

    private String address;
    private String info;

    @Builder.Default
    @OneToMany(mappedBy = "place")
    private List<Menu> menus = new ArrayList<>();

    public void addMenu(Menu menu){
        menus.add(menu);
    }

    // 연관관계 메서드 //
    public void confirmUser(User user){
        this.user = user;
        user.addPlace(this);
    }


}
