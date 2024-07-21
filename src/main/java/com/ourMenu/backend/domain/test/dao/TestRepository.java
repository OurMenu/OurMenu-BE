package com.ourMenu.backend.domain.test.dao;

import com.ourMenu.backend.domain.test.domain.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity,Long> {
}
