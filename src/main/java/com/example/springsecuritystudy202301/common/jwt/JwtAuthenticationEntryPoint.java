package com.example.springsecuritystudy202301.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("entry point 인증되지 않은 사용자(로그인 되지 않은 사용자) 요청 : {}", request.getRequestURI());
//        authException.printStackTrace();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
