package com.ourMenu.backend.init;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitConfig {

    @Bean
    public InitUser initUser(){
        return new InitUser();
    }
}
