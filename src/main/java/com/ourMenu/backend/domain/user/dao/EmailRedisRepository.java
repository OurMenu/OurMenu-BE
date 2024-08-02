package com.ourMenu.backend.domain.user.dao;

import com.ourMenu.backend.domain.user.domain.AuthEmailEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmailRedisRepository extends CrudRepository<AuthEmailEntity, String> {
}
