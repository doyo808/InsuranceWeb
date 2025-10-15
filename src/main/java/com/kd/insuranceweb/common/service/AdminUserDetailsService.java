package com.kd.insuranceweb.common.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kd.insuranceweb.common.dto.AdminUserDetails;
import com.kd.insuranceweb.common.dto.EmployeeDto;
import com.kd.insuranceweb.common.mapper.EmployeeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

	private final EmployeeMapper employeeMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 직원 기본 정보 조회
		EmployeeDto emp = employeeMapper.findByLoginId(username);
		if (emp == null) {
			throw new UsernameNotFoundException("직원을 찾을 수 없습니다: " + username);
		}

        // 2. 권한 목록 조회
        List<String> roleNames = employeeMapper.findRolesByEmpId(emp.getEmp_id());
        roleNames.add("ADMIN");
        
        List<GrantedAuthority> authorities;

        // 3. Spring Security 형태로 변환
        if (roleNames == null || roleNames.isEmpty()) {
            authorities = Collections.emptyList();
        } else {
            authorities = roleNames.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }

        // 4. UserDetails 객체 생성
		return new AdminUserDetails(emp.getEmp_id(),
								emp.getEmp_name(),
								emp.getUsername(),
								emp.getPassword(),
								authorities,
								employeeMapper.findRolesKorNameByEmpId(emp.getEmp_id()));
	}
}