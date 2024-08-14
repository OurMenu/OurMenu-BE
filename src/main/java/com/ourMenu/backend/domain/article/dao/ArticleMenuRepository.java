package com.ourMenu.backend.domain.article.dao;

import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMenuRepository extends JpaRepository<ArticleMenu, Long> {
}
