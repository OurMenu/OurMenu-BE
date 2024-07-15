package com.ourMenu.backend.domain.test.application;

import com.ourMenu.backend.domain.test.dao.TestRepository;
import com.ourMenu.backend.domain.test.domain.TestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    @Transactional
    public TestEntity saveTestEntity(TestEntity testEntity){
        return testRepository.save(testEntity);
    }

    @Transactional
    public TestEntity findTestEntity(Long testEntityId){
        return testRepository.findById(testEntityId).get();
    }
}
