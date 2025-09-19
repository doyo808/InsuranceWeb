package com.kd.insuranceweb.test.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	return http
    	        .authorizeHttpRequests(auth -> auth
    	            .requestMatchers("/login", "/public/**", "**").permitAll()
    	            .requestMatchers("/admin/**").hasRole("ADMIN")
    	            .anyRequest().authenticated()
    	        )
    	        .formLogin(form -> form
//    	            .loginPage("/login") // 필요하면 기본 로그인 페이지로 커스터마이징
    	            .permitAll()
    	        )
    	        .build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user1")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("1234"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
