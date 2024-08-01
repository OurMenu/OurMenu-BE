package com.ourMenu.backend.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 30)
public class RefreshTokenEntity {

    @Id
    private String refreshToken;
    private long userId;

}
