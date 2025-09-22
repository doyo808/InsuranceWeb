package com.kd.insuranceweb.common.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	String[] whiteList = {
    			"/", "/index.html", "/login", "/common/login.html", 
    			"/signup/**", "/common/error/**",
    			"/*/css/**", "/*/js/**", "/*/images/**",
    			"/api/auth/status"
    	};
    	
    	return http
    			.csrf(csrf -> csrf.disable())	// 개발환경 임시설정
    	        .authorizeHttpRequests(auth -> auth
    	            .requestMatchers(whiteList).permitAll()
    	            .requestMatchers("/admin/**").hasRole("ADMIN")
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
    

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
