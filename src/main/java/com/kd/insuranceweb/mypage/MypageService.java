package com.kd.insuranceweb.mypage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.CustomerMapper;
import com.kd.insuranceweb.common.mapper.PersonMapper;
import com.kd.insuranceweb.mypage.dto.ContractDto;
import com.kd.insuranceweb.mypage.dto.MarketingConsentDTO;
import com.kd.insuranceweb.mypage.mapper.MarketingConsentMapper;
import com.kd.insuranceweb.mypage.mapper.MyContractMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {
	
	private final PersonMapper personMapper;
	private final CustomerMapper customerMapper;
	private final MarketingConsentMapper marketingConsentMapper;
	private final MyContractMapper myContractMapper;

	// 고객정보 html로 전송
	public CustomUserDetails getPersonAndCustomerInfo(CustomUserDetails loginUser) {
		if (loginUser != null) {
			PersonDTO person = personMapper.selectById(loginUser.getPerson_id());
			CustomerDTO customer = customerMapper.selectByLoginId(loginUser.getUsername());

			if (person != null && customer != null) {
				loginUser.setPerson(person);
	            loginUser.setCustomer(customer);
			}
		}
		return loginUser;
	}
	// 내 정보 수정
	@Transactional
	public int editPersonAndCustomer(CustomerDTO customer) {
		int updatedPersonRows = editPerson(customer);
		int updatedCustomerRows = editCustomer(customer);
		return updatedPersonRows + updatedCustomerRows;
	}
	
	private int editPerson(CustomerDTO customer) {
		return personMapper.updatePerson(
								customer.getEmail(),
								customer.getPhone_number(), 
								customer.getPerson_id());
	}
	private int editCustomer(CustomerDTO customer) {
		return customerMapper.updateCustomer(
								customer.getZip_code(),
								customer.getAddress_1(),
								customer.getAddress_2(),
								customer.getHome_number(),
								customer.getJob(),
								customer.getCompany_name(),
								customer.getJob_zip_code(),
								customer.getJob_address1(),
								customer.getJob_address2(),
								customer.getJob_phone_number(),
								customer.getCustomer_id());
	}
	
	// 마케팅 동의 페이지 로딩시 DTO 주입
	public MarketingConsentDTO getMarketingConsentDTO(Integer customer_id) {
		return marketingConsentMapper.selectByCustomerId(customer_id);
	}
	// 마케팅 동의정보 등록, 수정
	public int saveOrUpdateMarketingConsent(Integer customer_id, MarketingConsentDTO dto) {
		dto.setCustomer_id(customer_id);
		dto.setUpdated_at(LocalDateTime.now());
		dto.setChannel_phone(dto.getChannel_phone() == null ? "N" : "Y");
		dto.setChannel_sms(dto.getChannel_sms() == null ? "N" : "Y");
		dto.setChannel_email(dto.getChannel_email() == null ? "N" : "Y");
		
		MarketingConsentDTO previousDTO = marketingConsentMapper.selectByCustomerId(customer_id);
		if (previousDTO == null) {
			return marketingConsentMapper.insertMarketingConsent(dto);
		} else {
			return marketingConsentMapper.updateMarketingConsent(dto);
		}
	}
	// 상태 코드 → 한글 매핑
    private static final Map<String, String> STATUS_MAP = Map.of(
        "PENDING", "신청",
        "ACTIVE", "유지",
        "EXPIRED", "만료",
        "APPROVED", "승인",
        "REJECTED", "반려"
    );

    public List<ContractDto> getAllContracts(Integer customer_id) {
        List<ContractDto> contracts = myContractMapper.selectAllContracts(customer_id);
        return translateStatus(contracts);
    }

    public List<ContractDto> getActiveContracts() {
        List<ContractDto> contracts = myContractMapper.selectActiveContracts();
        return translateStatus(contracts);
    }

    // 공통 변환 메서드
    private List<ContractDto> translateStatus(List<ContractDto> contracts) {
        contracts.forEach(c -> 
            c.setStatus(STATUS_MAP.getOrDefault(c.getStatus(), c.getStatus()))
        );
        return contracts;
    }
	
}
