package com.ourMenu.backend.domain.user.api;

import com.ourMenu.backend.domain.user.api.request.AuthEmailRequest;
import com.ourMenu.backend.domain.user.api.response.AuthEmailResponse;
import com.ourMenu.backend.domain.user.application.AccountService;
import com.ourMenu.backend.domain.user.application.EmailService;
import com.ourMenu.backend.global.common.ApiResponse;
import com.ourMenu.backend.global.util.ApiUtils;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ourMenu.backend.global.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final EmailService emailService;

    @PostMapping("/email")
    public ApiResponse<AuthEmailResponse> authEmail(@Valid @RequestBody AuthEmailRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(getErrorMessages(bindingResult));
        }
        String code = emailService.sendMail(request.getEmail());
        log.info("success sending! email: {} / code: {}", request.getEmail(), code);
        return ApiUtils.success(new AuthEmailResponse(code));
    }

}
