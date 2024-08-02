package com.ourMenu.backend.global.config;

import com.ourMenu.backend.global.argument_resolver.JwtAuthArgumentResolver;
import com.ourMenu.backend.global.interceptor.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;
    private final JwtAuthArgumentResolver jwtAuthArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/test/**", "/account/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**")
                .addPathPatterns("/account/logout");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthArgumentResolver);
    }

}
