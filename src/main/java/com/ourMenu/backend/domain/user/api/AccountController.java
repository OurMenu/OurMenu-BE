package com.ourMenu.backend.domain.user.api;

import com.ourMenu.backend.domain.user.api.request.AuthEmailRequest;
import com.ourMenu.backend.domain.user.api.request.ConfirmCodeRequest;
import com.ourMenu.backend.domain.user.api.request.LoginRequest;
import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.AuthEmailResponse;
import com.ourMenu.backend.domain.user.api.response.LoginResponse;
import com.ourMenu.backend.domain.user.application.AccountService;
import com.ourMenu.backend.domain.user.application.EmailService;
import com.ourMenu.backend.domain.user.exception.AuthEmailException;
import com.ourMenu.backend.domain.user.exception.EmailDuplicationException;
import com.ourMenu.backend.domain.user.exception.UserException;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.exception.ErrorCode;
import com.ourMenu.backend.global.exception.ErrorResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ourMenu.backend.global.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final EmailService emailService;

    @ExceptionHandler(AuthEmailException.class)
    public ResponseEntity<?> failedConfirmCode(AuthEmailException e) {
        return ApiUtils.error(ErrorResponse.of(ErrorCode.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(EmailDuplicationException.class)
    public ResponseEntity<?> duplicateEmail(EmailDuplicationException e) {
        return ApiUtils.error(ErrorResponse.of(ErrorCode.VALIDATION_ERROR, e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userException(UserException e) {
        return ApiUtils.error(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }

    @PostMapping("/email")
    public ApiResponse<AuthEmailResponse> authEmail(@Valid @RequestBody AuthEmailRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(getErrorMessages(bindingResult));
        }
        // TODO: Add logic to ensure email exists
        String code = emailService.sendMail(request.getEmail());
        log.info("success sending! email: {} / code: {}", request.getEmail(), code);
        return ApiUtils.success(new AuthEmailResponse(code));
    }

    @PostMapping("/confirmCode")
    public ApiResponse<Object> confirmCode(@Valid @RequestBody ConfirmCodeRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(getErrorMessages(bindingResult));
        }
        boolean confirmResult = emailService.confirmCode(request.getEmail(), request.getCode());
        if(!confirmResult) throw new AuthEmailException();
        return ApiUtils.success(null);
    }

    @PostMapping("/signup")
    public ApiResponse<LoginResponse> signup(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(getErrorMessages(bindingResult));
        }
        return ApiUtils.success(accountService.signup(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(getErrorMessages(bindingResult));
        }
        return ApiUtils.success(accountService.login(request));
    }

}
