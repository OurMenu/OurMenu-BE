package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.LoginResponse;
import com.ourMenu.backend.domain.user.dao.RefreshTokenRedisRepository;
import com.ourMenu.backend.domain.user.dao.UserDao;
import com.ourMenu.backend.domain.user.domain.RefreshTokenEntity;
import com.ourMenu.backend.domain.user.exception.EmailDuplicationException;
import com.ourMenu.backend.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRepository;

    private final String GRANT_TYPE = "Bearer";
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse signup(SignUpRequest request) {
        if(userDao.isEmailExists(request.getEmail()))
            throw new EmailDuplicationException();

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        long id = userDao.createUser(request);
        String accessToken = jwtProvider.createToken(id, 1);
        String refreshToken = jwtProvider.createToken(id, 24 * 30);

        RefreshTokenEntity entity = new RefreshTokenEntity(refreshToken, id);
        refreshTokenRepository.save(entity);

        Instant accessTokenExpiredAt = Instant.now().plus(1, ChronoUnit.HOURS);
        Instant refreshTokenExpiredAt = Instant.now().plus(30, ChronoUnit.DAYS);
        return new LoginResponse(GRANT_TYPE, accessToken, refreshToken, accessTokenExpiredAt, refreshTokenExpiredAt);
    }

}
