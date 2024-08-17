package com.ourMenu.backend.domain.article.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DownloadArticleMenu {

    private List<Long> articleMenuIds;
}
