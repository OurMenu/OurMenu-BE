package com.ourMenu.backend.domain.user.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "email", timeToLive = 300)
public class AuthEmailEntity {

    @Id
    String email;
    String code;

}
