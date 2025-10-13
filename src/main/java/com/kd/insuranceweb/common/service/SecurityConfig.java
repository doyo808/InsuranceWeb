package com.kd.insuranceweb.common.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // --- 관리자용 인증 Provider 설정 ---
    @Bean
    DaoAuthenticationProvider adminAuthenticationProvider(AdminUserDetailsService adminUserDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(adminUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        // 필요하다면 여기에 추가적인 인증 실패/성공 핸들러 등을 설정할 수 있습니다.
        return provider;
    }
    // --- 일반 사용자용 인증 Provider 설정 ---
    @Bean
    DaoAuthenticationProvider userAuthenticationProvider(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


    
    
    
    
    /**
     * 관리자용 SecurityFilterChain
     * @Order(1) : 필터 체인 우선순위를 1번으로 설정. 더 구체적인 경로가 먼저 검사되어야 합니다.
     */
    @Bean
    @Order(1)
    SecurityFilterChain adminFilterChain(HttpSecurity http, 
            @Qualifier("adminAuthenticationProvider") DaoAuthenticationProvider adminAuthenticationProvider) throws Exception {
    	String[] whiteList = {
    			"/admin/login", "/admin/main",
    			"/*/css/**", "/*/js/**", "/*/images/**"
    	};
        http
            .securityMatcher("/admin/**") // 이 필터 체인은 /admin/으로 시작하는 경로에만 적용됩니다.
            .authenticationProvider(adminAuthenticationProvider) // 관리자용 인증 Provider 사용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(whiteList).permitAll() // 관리자 로그인 페이지는 허용
                .anyRequest().hasRole("ADMIN")               // 그 외 모든 /admin/** 경로는 ADMIN 역할 필요
            )
            .formLogin(form -> form
                .loginPage("/admin/login")                   // 관리자 전용 로그인 페이지
                .loginProcessingUrl("/admin/login-process")  // 관리자 로그인 처리 URL
                .defaultSuccessUrl("/admin/main", true)      // 성공 시 관리자 메인 페이지로 이동
                .failureUrl("/admin/login?error=true")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) 
            .logout(logout -> logout
                .logoutUrl("/admin/logout")                  // 관리자 로그아웃 URL
                .logoutSuccessUrl("/admin/login")
            );
        return http.build();
    }

    /**
     * 일반 사용자용 SecurityFilterChain
     * @Order(2) : 관리자 필터 체인 다음으로 적용됩니다.
     */
    @Bean
    SecurityFilterChain userFilterChain(HttpSecurity http,
            @Qualifier("userAuthenticationProvider") DaoAuthenticationProvider userAuthenticationProvider) throws Exception {
    	String[] whiteList = {
    			"/", "/index.html", "/login", "/common/login.html", "/home",
    			"/signup/**", "/common/error/**",
    			"/*/css/**", "/*/js/**", "/*/images/**",
    			"/api/auth/status", "/cert/**",
    			"/helpdesk/**", "/terms/**", "/club/PP050101_001.html",

    			"/club/PP050301_001","/club/PP050401_001", "/club/PP060701_001","/club/event/index", "/club/event/analysis",

    	};
    	
    	return http
    			.csrf(csrf -> csrf
    		    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))	// 개발환경 임시설정
    			.authenticationProvider(userAuthenticationProvider)
    	        .authorizeHttpRequests(auth -> auth
    	            .requestMatchers(whiteList).permitAll()
    	            .anyRequest().authenticated()
    	        )
    	        .formLogin(form -> form
    	            .loginPage("/login")			// 컨트롤러의 매핑경로
    	            .loginProcessingUrl("/login-process")	// 로그인 form의 action
    	            .failureUrl("/login?error=true")
    	            .defaultSuccessUrl("/", true)	// true는 항상 해당 url, false면 로그인전 요청 페이지
    	            .permitAll()
    	        ).logout(logout -> logout
    	                .logoutUrl("/logout")			   // POST 요청 허용
    	                .logoutSuccessUrl("/") // 로그아웃 후 이동
    	                .invalidateHttpSession(true)
    	                .deleteCookies("JSESSIONID")
    	        )
    	        .build();
    }
    


}
