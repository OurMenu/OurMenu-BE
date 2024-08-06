package com.ourMenu.backend.domain.store.application;

import com.ourMenu.backend.domain.store.dao.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStoreService {

    private final UserStoreRepository userStoreRepository;


}
