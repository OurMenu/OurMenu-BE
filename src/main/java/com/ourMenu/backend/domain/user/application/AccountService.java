package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.LoginResponse;
import com.ourMenu.backend.domain.user.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final String GRANT_TYPE = "Bearer";
    private final UserDao userDao;

    public LoginResponse signup(SignUpRequest request) {
        long id = userDao.createUser(request);
        return new LoginResponse(GRANT_TYPE, "", "", Instant.now());
    }

}
