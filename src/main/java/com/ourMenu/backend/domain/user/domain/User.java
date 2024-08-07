package com.ourMenu.backend.domain.user.domain;

import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.menulist.domain.MenuList;
import com.ourMenu.backend.domain.menu.domain.Place;
import com.ourMenu.backend.global.common.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;
    private String password;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    @Column(name = "modified_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('CREATED', 'DELETED', 'UPDATED') DEFAULT 'CREATED'")
    @Builder.Default
    private Status status = Status.CREATED;

    @Lob
    private String imgUrl;

    @OneToMany(mappedBy = "user")
    private List<MenuList> menuLists;

    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    @OneToMany(mappedBy = "user")
    private List<Place> places;

    public void addMenuList(MenuList menuList) {
        menuLists.add(menuList);
    }

    public void addPlace(Place place){
        places.add(place);
    }

    public void addArticles(Article article){
        articles.add(article);
    }
}
