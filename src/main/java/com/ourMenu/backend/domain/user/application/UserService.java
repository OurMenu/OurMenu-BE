package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.user.api.request.ChangeNicknameRequest;
import com.ourMenu.backend.domain.user.api.request.ChangePasswordRequest;
import com.ourMenu.backend.domain.user.api.response.UserArticleResponse;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final S3Client s3Client;
    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

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

        String key = (String) user.get("img_url");
        String fileUrl = "";
        if(key != null && !key.isBlank()) {
            fileUrl = s3Client.utilities()
                    .getUrl(builder -> builder.bucket(bucketName).key(key))
                    .toExternalForm();
        }

        return new UserInfoResponse(userId, email, nickname, fileUrl);
    }

    public void uploadProfileImg(Long userId, MultipartFile file) {
        Map<String, Object> user = userDao.getUserById(userId);
        String originKey = (String) user.get("img_url");

        log.info("{} / {}", file.getOriginalFilename(), file.getContentType());
        String fileName = "";

        try {
            if(originKey != null && !originKey.isBlank()) {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                                .bucket(bucketName).key(originKey).build());
            }

            if(file != null && !file.isEmpty()) {
                fileName = "userProfile" + userId + "_" + UUID.randomUUID() + URLEncoder.encode(Objects.requireNonNull(file.getOriginalFilename()), StandardCharsets.UTF_8);

                s3Client.putObject(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(fileName)
                                .build(),
                        RequestBody.fromBytes(file.getBytes()));
            }
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException();
        }

        userDao.updateProfileImg(userId, fileName);
    }

    public List<UserArticleResponse> getUserArticles(Long userId, Long startId, int size) {
        return userDao.getUserArticles(userId, startId, size);
    }

    @Transactional
    public Optional<User> getUserById(Long userId){
        return userRepository.findById(userId);
    }

}
