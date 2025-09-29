package com.kd.insuranceweb.mall;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kd.insuranceweb.common.controller.HomeController;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.PersonMapper;
import com.kd.insuranceweb.mall.MallControllerTemp.PaymentCallbackRequest;
import com.kd.insuranceweb.mall.dto.InsuredDTO;
import com.kd.insuranceweb.mall.dto.MallInsuredDetailDTO;
import com.kd.insuranceweb.mall.dto.MallPersonalBasicDTO;
import com.kd.insuranceweb.mall.mapper.InsuredMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MallServiceTemp {

	// 계약도 채결하고, 특약도 넣어야함...
	
	private final PersonMapper personMapper;
	private final InsuredMapper insuredMapper;
	
	// 사람과 피보험자를 등록하기 (피보험자의 성별과 나이는 임시값임)
	@Transactional
	public int insertPersonAndInsured(MallPersonalBasicDTO mpbDTO, MallInsuredDetailDTO midDTO) {
		int person_id = personExists(mpbDTO.getInsured_email());
		if (person_id == -1) {person_id = insertPerson(mpbDTO);}
		int insured_id = insertInsured(person_id, "M", 30, midDTO);
		return 1;
	}
	
	// 등록된 사람여부 확인
	public int personExists(String insured_email) {
		PersonDTO person = personMapper.selectByEmail(insured_email);
		if (person == null) {
			return -1;
		} else return person.getPerson_id();
	}
	// 없다면 등록하기
	public int insertPerson(MallPersonalBasicDTO mpbDTO) {
		PersonDTO person = new PersonDTO();
		person.setPerson_name(mpbDTO.getInsured_name());
		person.setPhone_number(mpbDTO.getInsured_phone_number());
		person.setEmail(mpbDTO.getInsured_email());
		person.setPersonal_id("");
		personMapper.insertPerson(person);
		return person.getPerson_id();
	}
	
	// 피보험자 등록하고 id를 반환
	public int insertInsured(
			Integer person_id, String gender, Integer age, MallInsuredDetailDTO midDTO) {
		InsuredDTO insured = new InsuredDTO();
		insured.setPerson_id(person_id);
		insured.setGender(gender);
		insured.setAge(age);
		insured.setIs_smoker(midDTO.getIs_smoker());
		insured.setDrinks(midDTO.getDrinks());
		insured.setDriving_status(midDTO.getDriving_status());
		insured.setMedical_history(midDTO.getMedical_history().equals("Y") ? midDTO.getMedical_history_text() : "병력없음");
		insuredMapper.insertInsured(insured);
		return insured.getInsured_id();
	}
	
}
