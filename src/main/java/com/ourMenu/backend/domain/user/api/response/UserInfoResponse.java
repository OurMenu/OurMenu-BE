package com.ourMenu.backend.domain.user.api.response;

public record UserInfoResponse(long userId,
                               String email,
                               String nickname,
                               String imgUrl
) { }
