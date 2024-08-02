package com.ourMenu.backend.domain.user.api.response;

import java.time.Instant;

public record LoginResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        Instant accessTokenExpiredAt,
        Instant refreshTokenExpiredAt
) { }
