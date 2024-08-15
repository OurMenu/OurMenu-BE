package com.ourMenu.backend.domain.article.application;

import com.ourMenu.backend.domain.article.dao.ArticleMenuRepository;
import com.ourMenu.backend.domain.article.domain.ArticleMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleMenuService {

    private final ArticleMenuRepository articleMenuRepository;

    public ArticleMenu save(ArticleMenu articleMenu){
        return articleMenuRepository.save(articleMenu);
    }
}
