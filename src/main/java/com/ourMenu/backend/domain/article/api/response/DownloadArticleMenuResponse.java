package com.ourMenu.backend.domain.article.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DownloadArticleMenuResponse {

    public Long menuGroupId;

    public static DownloadArticleMenuResponse toDto(Long groupId) {
        return DownloadArticleMenuResponse.builder()
                .menuGroupId(groupId)
                .build();
    }
}
