package com.ourMenu.backend.global.interceptor;

import com.ourMenu.backend.global.util.JwtProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final String GRANT_TYPE = "Bearer ";
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String accessToken = resolveAccessToken(request);
        validateAccessToken(accessToken);
        request.setAttribute("jwtToken", accessToken);

        return true;
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateToken(token);
        return token.substring(GRANT_TYPE.length());
    }

    private void validateToken(String token) {
        if (token == null) {
            throw new JwtException("토큰이 없습니다");
        }
        if (!token.startsWith(GRANT_TYPE)) {
            throw new JwtException("지원하지 않는 형식의 토큰입니다");
        }
    }

    private void validateAccessToken(String accessToken) {
        if (!jwtProvider.isValidToken(accessToken)) {
            throw new JwtException("");
        }
    }

}
