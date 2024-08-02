package com.ourMenu.backend.domain.user.dao;

import com.ourMenu.backend.domain.user.domain.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenEntity, Long> {
}
