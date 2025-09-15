package com.kd.insuranceweb.common.service;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.common.dto.Person;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupService {
	
	private final PersonMapper personMapper;
	
	/**
     * 이메일 사용 가능 여부를 확인합니다.
     * @param email 확인할 이메일
     * @return 사용 가능하면 true, 이미 존재하면 false
     */
	public boolean isEmailAvailable(String email) {
		Person person = personMapper.selectByEmail(email);
		return person == null;
	}
}
