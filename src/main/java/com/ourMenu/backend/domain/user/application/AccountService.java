package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.LoginResponse;
import com.ourMenu.backend.domain.user.dao.UserDao;
import com.ourMenu.backend.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final String GRANT_TYPE = "Bearer";
    private final UserDao userDao;
    private final JwtProvider jwtProvider;

    public LoginResponse signup(SignUpRequest request) {
        long id = userDao.createUser(request);
        String accessToken = jwtProvider.createToken(id, 1);
        String refreshToken = jwtProvider.createToken(id, 24 * 30);
        return new LoginResponse(GRANT_TYPE, accessToken, refreshToken, Instant.now());
    }

}
