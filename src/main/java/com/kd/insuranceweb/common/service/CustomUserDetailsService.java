package com.kd.insuranceweb.common.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.CustomerMapper;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final CustomerMapper customerMapper;
	private final PersonMapper personMapper;
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
		
        // 로그인시도한 id로 고객을 찾는 과정
		CustomerDTO customer = customerMapper.selectById(login_id);
		if (customer == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + login_id);
		}
		
		// customer에 있는 person_id로 고객명을 찾고, 주입
		PersonDTO person = personMapper.selectById(customer.getPerson_id());
		customer.setPerson_name(person.getPerson_name());
		customer.setPhone_number(person.getPhone_number());
		customer.setEmail(person.getEmail());
		System.out.println(customer);
		
		return new CustomUserDetails(customer);
	}
	
}
