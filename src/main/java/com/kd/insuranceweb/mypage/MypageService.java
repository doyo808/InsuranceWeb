package com.kd.insuranceweb.mypage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
import com.kd.insuranceweb.mypage.dto.PaymentDto;
import com.kd.insuranceweb.mypage.mapper.MarketingConsentMapper;
import com.kd.insuranceweb.mypage.mapper.MyContractMapper;
import com.kd.insuranceweb.mypage.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {
	
	private final PersonMapper personMapper;
	private final CustomerMapper customerMapper;
	private final MarketingConsentMapper marketingConsentMapper;
	private final MyContractMapper myContractMapper;
	private final PaymentMapper paymentMapper;

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
	    MarketingConsentDTO dto = marketingConsentMapper.selectByCustomerId(customer_id);

	    if (dto == null) {
	        dto = new MarketingConsentDTO();
	    }

	    if (dto.getConsent_collection() == null) dto.setConsent_collection("N");
	    if (dto.getConsent_marketing() == null) dto.setConsent_marketing("N");
	    if (dto.getConsent_sharing() == null) dto.setConsent_sharing("N");
	    if (dto.getConsent_lookup() == null) dto.setConsent_lookup("N");

	    return dto;
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
	// 상태 코드 → 한글 매핑 (계약)
    private static final Map<String, String> STATUS_MAP = Map.of(
        "PENDING", "신청",
        "ACTIVE", "유지",
        "EXPIRED", "만료",
        "APPROVED", "승인",
        "REJECTED", "반려",
        "CANCELLED", "취소"
    );

    public List<ContractDto> getAllContracts(Integer customer_id) {
        List<ContractDto> contracts = myContractMapper.selectAllContracts(customer_id);
        return translateStatus(contracts);
    }

    public List<ContractDto> getActiveContracts(Integer customer_id) {
        List<ContractDto> contracts = myContractMapper.selectActiveContracts(customer_id);
        return translateStatus(contracts);
    }

    // 공통 변환 메서드
    private List<ContractDto> translateStatus(List<ContractDto> contracts) {
        contracts.forEach(c -> 
            c.setStatus(STATUS_MAP.getOrDefault(c.getStatus(), c.getStatus()))
        );
        return contracts;
    }
	

	// 상태 코드 → 한글 매핑 (납부)
    private static final Map<String, String> PAY_STATUS_MAP = Map.of(
        "P", "완납",
        "A", "부분납부",
        "U", "미납",
        "O", "초과납부"
    );
    public List<PaymentDto> getPayments(Integer customer_id) {
    	List<PaymentDto> payments = paymentMapper.selectPayments(customer_id);
    	return translatePayStatus(payments);
    }
    // 공통 변환 메서드
    private List<PaymentDto> translatePayStatus(List<PaymentDto> payments) {
        YearMonth currentMonth = YearMonth.now();

        payments.forEach(p -> {
            LocalDate paymentDate = p.getPayment_date().toLocalDate();

            YearMonth paymentMonth = YearMonth.from(paymentDate);

            // 이번달 납입이 아니면 상태를 'U'로 변경
            if (!paymentMonth.equals(currentMonth)) { p.setPay_status("U"); }
            p.setPay_status(PAY_STATUS_MAP.getOrDefault(p.getPay_status(), p.getPay_status()));
        });
        return payments;
    }
    
}
