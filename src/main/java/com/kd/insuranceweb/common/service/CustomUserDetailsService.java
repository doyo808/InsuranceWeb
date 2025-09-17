package com.kd.insuranceweb.common.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.common.mapper.CustomerMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final CustomerMapper customerMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String login_id) throws UsernameNotFoundException {
		// 'admin' 아이디에 대한 특별 처리
        if ("admin".equals(login_id)) {
            // DB 조회 없이 메모리에서 직접 관리자 정보 생성
            return User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234")) // 비밀번호는 반드시 암호화
                .roles("ADMIN", "USER") // 여러 권한 부여 가능
                .build();
        }
		
		CustomerDTO customer = customerMapper.selectById(login_id);
		
		if (customer == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + login_id);
		}
		
		return new CustomUserDetails(customer);
	}
	
}
