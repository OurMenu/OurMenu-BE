package com.ourMenu.backend.domain.user.dao;

import com.ourMenu.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
