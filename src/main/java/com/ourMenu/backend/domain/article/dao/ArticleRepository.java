package com.ourMenu.backend.domain.article.dao;

import com.ourMenu.backend.domain.article.domain.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = {"articleMenuList"})
    Optional<Article> findById(Long id);
}
