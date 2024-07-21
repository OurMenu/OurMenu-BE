package com.ourMenu.backend.domain.test.application;

import com.ourMenu.backend.domain.test.dao.JdbcEntityDao;
import com.ourMenu.backend.domain.test.dao.TestRepository;
import com.ourMenu.backend.domain.test.domain.JdbcEntity;
import com.ourMenu.backend.domain.test.domain.TestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final JdbcEntityDao jdbcEntityDao;

    @Transactional
    public void saveJdbcEntity(String name){
        jdbcEntityDao.save(name);
    }

    @Transactional
    public JdbcEntity findJdbcEntity(Long jdbcEntityId){
        return jdbcEntityDao.findById(jdbcEntityId);
    }

    @Transactional
    public TestEntity saveTestEntity(TestEntity testEntity){
        return testRepository.save(testEntity);
    }

    @Transactional
    public TestEntity findTestEntity(Long testEntityId){
        return testRepository.findById(testEntityId).get();
    }
}
