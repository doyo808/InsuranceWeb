package com.kd.insuranceweb.common.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

/**
 * Ajax 요청 시 인증 실패(로그인 안된 상태) → 401 Unauthorized 응답
 * 일반 요청 시 → 로그인 페이지로 리다이렉트
 */
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Ajax 요청인 경우
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized (로그인이 필요합니다)");
        } else {
            // 일반 브라우저 접근인 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("/login");
        }
    }
}
