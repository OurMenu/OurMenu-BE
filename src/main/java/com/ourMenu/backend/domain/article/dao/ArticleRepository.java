package com.ourMenu.backend.domain.article.dao;

import com.ourMenu.backend.domain.article.domain.Article;
import com.ourMenu.backend.domain.menu.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = {"articleMenuList"})
    Optional<Article> findById(Long id);

    @Query("SELECT m FROM Article m " +
            "JOIN FETCH ArticleMenu " +
            "WHERE m.user.id = :userId " +
            "AND (:title IS NULL OR m.title LIKE %:title%) ")
        // groupId를 기준으로 오름차순 정렬
    Page<Article> findAllByUserAndTitleContaining(@Param("title") String title,
                                               @Param("userId") Long userId,
                                               Pageable pageable);
}
