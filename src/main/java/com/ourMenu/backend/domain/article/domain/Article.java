package com.ourMenu.backend.domain.article.domain;

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
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article")
    private List<ArticleMenu> articleMenuList = new ArrayList<>();

    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.CREATED;

    private String thumbnail;
    private Long views = 0L;

    // 연관관계 메서드 //
    public void confirmUser(User user) {
        this.user = user;
        user.addArticles(this);
    }

    public void addArticleMenu(ArticleMenu articleMenu) {
        this.addArticleMenu(articleMenu);
    }
}
