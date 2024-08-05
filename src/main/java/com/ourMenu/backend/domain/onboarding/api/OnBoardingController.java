package com.ourMenu.backend.domain.onboarding.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnBoardingController {

    @GetMapping
    public String getOnboarding(){
        return "success";
    }
}
