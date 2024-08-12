package com.ourMenu.backend.init;

import com.ourMenu.backend.domain.user.api.AccountController;
import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.LoginResponse;
import com.ourMenu.backend.domain.user.application.AccountService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InitUser {

    @Autowired
    private AccountService accountService;

    @PostConstruct
    public void init() {
        SignUpRequest signUpRequest = new SignUpRequest();
        UUID uuid = UUID.randomUUID();
        signUpRequest.setEmail(uuid + "@naver.com");
        signUpRequest.setNickname("더미유저");
        signUpRequest.setPassword("q1w2e3r4");
        LoginResponse signup = accountService.signup(signUpRequest);
        System.out.println("더미 유저 엑세스 토큰: " + signup.accessToken());
    }

}
