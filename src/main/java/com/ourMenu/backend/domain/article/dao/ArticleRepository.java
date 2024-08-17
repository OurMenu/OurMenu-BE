package com.ourMenu.backend.domain.article.dao;

import com.ourMenu.backend.domain.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = {"articleMenuList"})
    Optional<Article> findById(Long id);

    @EntityGraph(attributePaths = {"articleMenuList"})
    @Query("SELECT m FROM Article m " +
            "WHERE (:title IS NULL OR m.title LIKE %:title%)")
    Page<Article> findAllByTitleContaining(@Param("title") String title, Pageable pageable);

    @EntityGraph(attributePaths = {"articleMenuList"})
    @Query("SELECT m FROM Article m " +
            "WHERE (:title IS NULL OR m.title LIKE %:title%)" +
            "AND m.user.id = :userId")
    Page<Article> findAllByUserAndTitleContaining(@Param("title") String title, Pageable pageable, Long userId);
}
