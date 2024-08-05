package com.ourMenu.backend.domain.store.dao;

import com.ourMenu.backend.domain.store.domain.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStoreRepository extends JpaRepository<UserStore,Long> {
    Optional<UserStore> findByUserIdAndStoreId(Long userId, String storeId);

    List<UserStore> findByUserIdOrderByModifiedAtDesc(Long userId);
}
