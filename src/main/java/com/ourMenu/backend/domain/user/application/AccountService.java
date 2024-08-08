package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.menulist.application.MenuListService;
import com.ourMenu.backend.domain.menulist.dto.request.MenuListRequestDTO;
import com.ourMenu.backend.domain.user.api.request.LoginRequest;
import com.ourMenu.backend.domain.user.api.request.ReissueTokenRequest;
import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.LoginResponse;
import com.ourMenu.backend.domain.user.dao.RefreshTokenRedisRepository;
import com.ourMenu.backend.domain.user.dao.UserDao;
import com.ourMenu.backend.domain.user.domain.RefreshTokenEntity;
import com.ourMenu.backend.domain.user.exception.EmailDuplicationException;
import com.ourMenu.backend.domain.user.exception.UserException;
import com.ourMenu.backend.global.exception.ErrorCode;
import com.ourMenu.backend.global.util.JwtProvider;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRepository;

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MenuListService menuListService;

    private LoginResponse makeLoginResponse(long id) {
        String GRANT_TYPE = "Bearer";
        String accessToken = jwtProvider.createToken(id, 1);
        String refreshToken = jwtProvider.createToken(id, 24 * 30);

        RefreshTokenEntity entity = new RefreshTokenEntity(id, refreshToken);
        refreshTokenRepository.save(entity);

        Instant accessTokenExpiredAt = Instant.now().plus(1, ChronoUnit.HOURS);
        Instant refreshTokenExpiredAt = Instant.now().plus(30, ChronoUnit.DAYS);

        return new LoginResponse(GRANT_TYPE, accessToken, refreshToken, accessTokenExpiredAt, refreshTokenExpiredAt);
    }

    @Transactional
    public LoginResponse signup(SignUpRequest request) {
        // validate email
        if(userDao.isEmailExists(request.getEmail()))
            throw new EmailDuplicationException();

        // encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        long id = userDao.createUser(request);

        // 기본 메뉴판 생성
        MenuListRequestDTO menuListRequestDTO = MenuListRequestDTO.builder()
                .menuFolderIcon("1")
                .menuFolderTitle("기본 메뉴판")
                .menuFolderImg(java.util.Optional.empty())
                .menuId(Collections.emptyList())
                .build();
        menuListService.createMenuList(menuListRequestDTO, id);

        return makeLoginResponse(id);
    }

    public LoginResponse login(LoginRequest request) {
        Map<String, Object> result;

        // get user by email
        try {
            result = userDao.getUserByEmail(request.getEmail());
        } catch(EmptyResultDataAccessException e) {
            throw new UserException(ErrorCode.EMAIL_NOT_FOUND_ERROR);
        }

        // validate password
        String encodedPassword = (String) result.get("password");
        if(!passwordEncoder.matches(request.getPassword(), encodedPassword)) {
            throw new UserException(ErrorCode.INVALID_PASSWORD_ERROR);
        }

        return makeLoginResponse((long)result.get("user_id"));
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }

    public LoginResponse reissueToken(ReissueTokenRequest request) {
        long id = jwtProvider.getUserId(request.getRefreshToken());

        Optional<RefreshTokenEntity> tokenEntity = refreshTokenRepository.findById(id);
        if(tokenEntity.isEmpty())
            throw new JwtException("");
        if(!tokenEntity.get().getRefreshToken().equals(request.getRefreshToken()))
            throw new JwtException("");

        return makeLoginResponse(id);
    }

}
