package com.ourMenu.backend.domain.user.api.response;

public record UserArticleResponse(int id,
                                  String title,
                                  String content,
                                  String creator,
                                  String profileImgUrl,
                                  String createdAt,
                                  int menusCount,
                                  int views,
                                  String thumbnail) {
}
