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
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    @Builder.Default
    private List<ArticleMenu> articleMenuList = new ArrayList<>();

    private String title;
    private String content;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.CREATED;


    @Builder.Default
    private int views = 0;

    // 연관관계 메서드 //
    public void confirmUser(User user) {
        this.user = user;
        user.addArticles(this);
    }

    public void addArticleMenu(ArticleMenu articleMenu) {
        this.articleMenuList.add(articleMenu);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void update(Article article){
        this.title=article.getTitle();
        this.content=article.getContent();
        this.modifiedAt=LocalDateTime.now();
        this.status=Status.UPDATED;
    }

    public void visit(){
        this.views++;
    }
}
