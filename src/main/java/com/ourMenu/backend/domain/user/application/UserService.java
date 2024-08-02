package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.user.api.request.ChangePasswordRequest;
import com.ourMenu.backend.domain.user.dao.UserDao;
import com.ourMenu.backend.domain.user.exception.UserException;
import com.ourMenu.backend.global.exception.ErrorCode;
import com.ourMenu.backend.global.exception.ErrorResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

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

}
