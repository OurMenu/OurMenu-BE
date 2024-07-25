package com.ourMenu.backend.domain.user.api;

import com.ourMenu.backend.domain.user.application.AccountService;
import com.ourMenu.backend.domain.user.application.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final EmailService emailService;

}
