package com.ourMenu.backend.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "email", timeToLive = 300)
public class AuthEmailEntity {

    @Id
    private String email;
    private String code;

}
