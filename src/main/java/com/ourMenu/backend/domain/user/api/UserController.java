package com.ourMenu.backend.domain.user.api;

import com.ourMenu.backend.domain.user.api.request.ChangeNicknameRequest;
import com.ourMenu.backend.domain.user.api.request.ChangePasswordRequest;
import com.ourMenu.backend.domain.user.api.response.UserArticleResponse;
import com.ourMenu.backend.domain.user.api.response.UserInfoResponse;
import com.ourMenu.backend.domain.user.application.UserService;
import com.ourMenu.backend.global.argument_resolver.UserId;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import static com.ourMenu.backend.global.util.BindingResultUtils.getErrorMessages;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/password")
    public ApiResponse<Object> changePassword(@UserId Long userId, @Valid @RequestBody ChangePasswordRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(getErrorMessages(bindingResult));
        }
        userService.changePassword(userId, request);
        return ApiUtils.success(null);
    }

    @PatchMapping("/nickname")
    public ApiResponse<Object> changeNickname(@UserId Long userId, @Valid @RequestBody ChangeNicknameRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(getErrorMessages(bindingResult));
        }
        userService.changeNickname(userId, request);
        return ApiUtils.success(null);
    }

    @GetMapping("")
    public ApiResponse<UserInfoResponse> getUserInfo(@UserId Long userId) {
        return ApiUtils.success(userService.getUserInfo(userId));
    }

    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Object> uploadProfileImg(@UserId Long userId, @RequestParam("imgFile") MultipartFile file) {
        userService.uploadProfileImg(userId, file);
        return ApiUtils.success(null);
    }

    @GetMapping("/myPost")
    public ApiResponse<List<UserArticleResponse>> getMyPost(
            @UserId Long userId,
            @RequestParam(value = "id", required = false) Long startId,
            @RequestParam(value = "size", required = false) Integer size) {
        long finalId = (startId != null) ? startId : Long.MAX_VALUE;
        int finalSize = (size != null && size > 0) ? size : 10;

        return ApiUtils.success(userService.getUserArticles(userId, finalId, finalSize));
    }

}
