package com.kd.insuranceweb.mall;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.PersonMapper;
import com.kd.insuranceweb.mall.dto.CoverageTempDto;
import com.kd.insuranceweb.mall.dto.InsuranceApplyDto;
import com.kd.insuranceweb.mall.dto.InsuredDTO;
import com.kd.insuranceweb.mall.dto.MallInsuredDetailDTO;
import com.kd.insuranceweb.mall.dto.MallPersonalBasicDTO;
import com.kd.insuranceweb.mall.mapper.ContractMapper;
import com.kd.insuranceweb.mall.mapper.CoverageMapper;
import com.kd.insuranceweb.mall.mapper.InsuredMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MallServiceTemp {

	private final PersonMapper personMapper;
	private final InsuredMapper insuredMapper;
	private final ContractMapper contractMapper;
	private final CoverageMapper coverageMapper;
	
	// 계약완료(피보험자 기본정보, 상세정보 등록 -> 계약 등록 -> 보장항목 등록)
	@Transactional
	public int completeContract(MallPersonalBasicDTO mpbDTO, MallInsuredDetailDTO midDTO, InsuranceApplyDto iaDto, Integer customer_id) {
		// 사람,피보험자 항목 체크 후 등록
		int person_id = personExists(mpbDTO.getInsured_email());
		if (person_id == -1) {person_id = insertPerson(mpbDTO);}
		int insured_id = insertInsured(person_id, "M", 30, midDTO);
		
		// 계약내용과 보장항목 추가
		insertContractAndCoverages(iaDto, customer_id, insured_id);
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
		insured.setMedical_history(midDTO.getMedical_history().equals("Y") ? Objects.toString(midDTO.getMedical_history_text(), "") : "병력없음");
		insuredMapper.insertInsured(insured);
		return insured.getInsured_id();
	}

	// 계약과 보장항목들 넣기
	public int insertContractAndCoverages(InsuranceApplyDto iaDto, Integer customer_id, Integer insured_id) {
		 // 1. MyBatis에 전달할 파라미터를 담을 Map을 생성합니다.
	    Map<String, Object> contractParams = new HashMap<>();
	    contractParams.put("product_id", iaDto.getProductId());
	    contractParams.put("customer_id", customer_id);
	    contractParams.put("insured_id", insured_id);
	    contractParams.put("total_premium", new BigDecimal(iaDto.getTotalPremium()));
	    
	    // 2. 수정된 Mapper 메소드를 호출합니다.
	    //    이 메소드가 실행된 직후, contractParams 맵에는 'contract_id'가 채워집니다.
	    int insertedCount = contractMapper.insertContract(contractParams);
	    
	    // 3. Map에서 <selectKey>가 생성해준 contract_id를 꺼냅니다.
	    //    keyProperty="contract_id" 와 일치하는 key("contract_id")를 사용합니다.
	    Integer contract_id = (Integer) contractParams.get("contract_id");

	    // ID가 정상적으로 반환되었는지 확인 (안정성을 위해)
	    if (contract_id == null) {
	        throw new RuntimeException("계약 ID를 생성하지 못했습니다.");
	    }
	    
	    // 4. 반환받은 contract_id를 사용해 나머지 로직을 실행합니다.
	    List<CoverageTempDto> coverages = iaDto.getSelectedCoverages();
	    for (CoverageTempDto c : coverages) {
	        // coverageMapper에도 contract_id를 넘겨줍니다.
	        coverageMapper.insertCoverage(contract_id, c.getId(), new BigDecimal(c.getAmount()));
	        insertedCount++; // 계약 1건 + 담보 n건 = 총 INSERT된 수
	    }
	    
	    return insertedCount;
	}

	
	

	
}
