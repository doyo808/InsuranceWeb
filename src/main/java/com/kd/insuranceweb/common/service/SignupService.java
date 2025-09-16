package com.kd.insuranceweb.common.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.dto.SignupStep1DTO;
import com.kd.insuranceweb.common.dto.SignupStep2DTO;
import com.kd.insuranceweb.common.mapper.CustomerMapper;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupService {
	
	private final PersonMapper personMapper;
	private final CustomerMapper customerMapper;
	private final PasswordEncoder passwordEncoder;
	
	/**
     * 이메일 사용 가능 여부를 확인합니다.
     * @param email 확인할 이메일
     * @return 사용 가능하면 true, 이미 존재하면 false
     */
	public boolean isEmailAvailable(String email) {
		PersonDTO person = personMapper.selectByEmail(email);
		return person == null;
	}
	
	public boolean isloginIdAvailable(String login_id) {
		CustomerDTO customer = customerMapper.selectById(login_id);
		return customer == null;
	}
	
	 /**
     * 회원가입 처리를 위한 통합 서비스 메서드.
     * Person 정보와 Customer(로그인) 정보를 하나의 트랜잭션으로 DB에 저장합니다.
     * @param dto1 1단계 폼에서 받은 개인 정보
     * @param dto2 2단계 폼에서 받은 로그인 정보
     * @return 생성된 person_id
     */
	@Transactional
	public int insertPersonAndCustomer(SignupStep1DTO dto1, SignupStep2DTO dto2) {
		// persons 테이블에 데이터 넣기
		String personal_id = dto1.getJumin6() + "-" + dto1.getJumin7();
		String phone_number = dto1.getPhoneNumber().replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
		
		PersonDTO person = new PersonDTO();
			person.setPerson_name(dto1.getKorName());
			person.setEmail(dto1.getEMail());
			person.setPersonal_id(personal_id);
			person.setPhone_number(phone_number);
		personMapper.insertPerson(person);
		
		// customers 테이블에 데이터 넣기
		String encodedPassword = passwordEncoder.encode(dto2.getPassword());
		String marketingYN = dto2.isMarketingAgree() ? "Y" : "N";
		
		CustomerDTO customer = new CustomerDTO();
			customer.setLogin_id(dto2.getLogin_id());
			customer.setPassword_hash(encodedPassword);
			customer.setPerson_id(person.getPerson_id());
			customer.setMarketing_agree_yn(marketingYN);
		customerMapper.insertCustomer(customer);
		
		return customer.getCustomer_id();
	}
}
