package com.tk.gg.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tk.gg.security.jwt.JwtProvider;
import com.tk.gg.security.jwt.TokenStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/*
 * 토큰 인증 실패 처리
 * */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtProvider jwtProvider;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        Optional<String> token = jwtProvider.resolveToken(request);

        if (token.isEmpty()) {
            unauthorizedWithMessage(response, "토큰 값 비어 있음");
        } else if (jwtProvider.validateAccessToken(token.get()).equals(TokenStatus.EXPIRED)) {
            makeTokenResponse(response, HttpStatus.FORBIDDEN, true);
        } else if (jwtProvider.validateAccessToken(token.get()).equals(TokenStatus.INVALID)) {
            makeTokenResponse(response, HttpStatus.UNAUTHORIZED, false);
        } else {
            log.error("CustomAuthenticationEntryPoint: 처리하지 못한 케이스의 예외");
        }
    }

    private void makeTokenResponse(HttpServletResponse response, HttpStatus status, boolean canRefresh) throws IOException {
        response.setStatus(status.value());
        response.getWriter().write(mapper.writeValueAsString(new TokenRefreshStatusResponse(canRefresh)));
    }

    private void unauthorizedWithMessage(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(mapper.writeValueAsString(message));
    }

    @Getter
    @NoArgsConstructor
    class TokenRefreshStatusResponse {
        private boolean success = false;
        private String message = "토큰 이상";
        private boolean canRefresh;

        public TokenRefreshStatusResponse(boolean canRefresh) {
            this.canRefresh = canRefresh;
        }
    }
}
