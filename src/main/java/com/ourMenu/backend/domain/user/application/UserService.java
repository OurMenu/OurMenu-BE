package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.user.api.request.ChangeNicknameRequest;
import com.ourMenu.backend.domain.user.api.request.ChangePasswordRequest;
import com.ourMenu.backend.domain.user.api.response.UserInfoResponse;
import com.ourMenu.backend.domain.user.dao.UserDao;
import com.ourMenu.backend.domain.user.dao.UserRepository;
import com.ourMenu.backend.domain.user.domain.User;
import com.ourMenu.backend.domain.user.exception.UserException;
import com.ourMenu.backend.global.exception.ErrorCode;
import com.ourMenu.backend.global.exception.ErrorResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userException(UserException e) {
        return ApiUtils.error(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        Map<String, Object> user = userDao.getUserById(userId);

        // validate password
        String encodedPassword = (String) user.get("password");
        if(!passwordEncoder.matches(request.getPassword(), encodedPassword)) {
            throw new UserException(ErrorCode.INVALID_PASSWORD_ERROR);
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        userDao.updatePassword(userId, encodedNewPassword);
    }

    public void changeNickname(Long userId, ChangeNicknameRequest request) {
        userDao.updateNickname(userId, request.getNickname());
    }

    public UserInfoResponse getUserInfo(Long userId) {
        Map<String, Object> user = userDao.getUserById(userId);

        String email = (String) user.get("email");
        String nickname = (String) user.get("nickname");
        String imgUrl = (String) user.get("img_url");

        return new UserInfoResponse(userId, email, nickname, imgUrl);
    }

    @Transactional
    public Optional<User> getUserById(Long userId){
        return userRepository.findById(userId);
    }

}
