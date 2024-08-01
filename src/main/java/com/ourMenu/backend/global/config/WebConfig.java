package com.ourMenu.backend.global.config;

import com.ourMenu.backend.global.interceptor.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/test/**")
                .excludePathPatterns("/account/**")
                .addPathPatterns("/account/logout");
    }

}
