package com.ourMenu.backend.domain.store.dao;

import com.ourMenu.backend.domain.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {
    Optional<Store> findByName(String name);
    Page<Store> findByMenuNameContaining(String menuName, Pageable pageable);
    Page<Store> findByNameContaining(String name, Pageable pageable);
}
