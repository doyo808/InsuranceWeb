package com.kd.insuranceweb.mall;

import java.math.BigDecimal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.PersonMapper;
import com.kd.insuranceweb.mall.dto.InsuranceApplyDto;
import com.kd.insuranceweb.mall.dto.MallInsuredDetailDTO;
import com.kd.insuranceweb.mall.dto.MallPersonalBasicDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/*
 * 보험 상품 가입 플로우를 처리하는 임시 컨트롤러.
 * 상품 선택부터 계약 완료까지의 과정을 단계별로 담당합니다.
 */
@Controller
@RequestMapping("/mall2")
@RequiredArgsConstructor
public class MallControllerTemp {
	
	private final MallServiceTemp mallServiceTemp;
	private final MallPaymentService mallPaymentService;
	private final PersonMapper personMapper;
	
	/*
	 * Step 1: 상품 선택 및 담보/보험료 정보 초기화
	 * 사용자가 선택한 상품과 담보 정보를 세션에 저장합니다.
	 */
	@GetMapping("/init")
	public String init() {
		return "mall/product_coverage";
	}
	@PostMapping("/init")
	@ResponseBody
	public String applyInsurance(@RequestBody InsuranceApplyDto applyDto, HttpSession session) {
		session.setAttribute("init", applyDto);
		return "OK";
	}
	
	/*
	 * Step 2: 계약자 및 피보험자 기본 정보 입력
	 * 로그인한 사용자의 정보를 바탕으로 계약자 정보를 설정하고, 피보험자 정보를 입력받습니다.
	 */
	@GetMapping("/info")
	public String getPersonalBasic(@AuthenticationPrincipal CustomUserDetails loginUser, Model model, HttpSession session) {
		if (loginUser != null) {
	        PersonDTO person = personMapper.selectById(loginUser.getPerson_id());
	        if (person != null) {
	            loginUser.setPerson(person);
	            model.addAttribute("loginUser", loginUser);
	            session.setAttribute("customer_id", loginUser.getCustomer_id());
	        }
	    }
		return "mall/personal_basic";
	}
	@PostMapping("/info")
	public String postPersonalBasic(MallPersonalBasicDTO dto, HttpSession session) {
		session.setAttribute("insured_name", dto.getInsured_name());
		session.setAttribute("insured_phone_number", dto.getInsured_phone_number());
		session.setAttribute("insured_email", dto.getInsured_email());
		session.setAttribute("customer_name", dto.getCustomer_name());
		session.setAttribute("MallPersonalBasicDTO", dto);
		
		return "redirect:/mall2/detail";
	}
	
	/*
	 * Step 3: 피보험자 상세 고지 정보 입력
	 * 피보험자의 건강, 운전 여부 등 상세 정보를 입력받습니다.
	 */
	@GetMapping("/detail")
	public String getInsuredDetail() {
		return "mall/insured_detail";
	}
	@PostMapping("/detail")
	public String postInsuredDetail(MallInsuredDetailDTO dto, HttpSession session) {
		session.setAttribute("is_smoker", dto.getIs_smoker());
		session.setAttribute("drinks", dto.getDrinks());
		session.setAttribute("driving_status", dto.getDriving_status());
		session.setAttribute("medical_history", dto.getMedical_history());
		session.setAttribute("medical_history_text", dto.getMedical_history_text());
		session.setAttribute("MallInsuredDetailDTO", dto);
		return "redirect:/mall2/notice";
	}
	
	/*
	 * Step 4: 계약 전 확인 및 고지 사항 안내
	 * 최종 계약에 앞서 입력된 모든 정보를 확인시켜주는 페이지입니다.
	 */
	@GetMapping("/notice")
	public String noticeContract(HttpSession session, Model model) {
		String insuredName = (String) session.getAttribute("insured_name");
		String customerName = (String) session.getAttribute("customer_name");
		String productName = (String) session.getAttribute("product_name");
		BigDecimal premium = (BigDecimal) session.getAttribute("premium");
		
		InsuranceApplyDto applyDto = (InsuranceApplyDto) session.getAttribute("init");
		if (applyDto == null) { return "redirect:/mall2/init"; }
		productName = applyDto.getProductName();
		premium = new BigDecimal(applyDto.getTotalPremium());
		
		if (insuredName == null) {
			insuredName = "";
			session.setAttribute("insured_name", insuredName);
		}
		if (customerName == null) {
			customerName = "임시 계약자";
			session.setAttribute("customer_name", customerName);
		}
		if (productName == null) {
			productName = "임시 상품";
			session.setAttribute("product_name", productName);
		}
		if (premium == null) {
			premium = new BigDecimal("12345");
			session.setAttribute("premium", premium);
		}
		model.addAttribute("insured_name", insuredName);
		model.addAttribute("customer_name", customerName);
		model.addAttribute("product_name", productName);
		model.addAttribute("premium", premium);
		model.addAttribute("selectionData", applyDto);
		return "mall/notice_contract";
	}
	
	/*
	 * Step 5: 결제 및 계약 최종 완료 처리
	 * 결제 정보를 입력받고, 서비스 로직을 호출하여 계약 정보를 DB에 저장합니다.
	 */
	@GetMapping("/payment")
	public String paymentForm() {
		return "mall/payment_form";
	}
	@PostMapping("/payment")
	public String paymentComplete(HttpSession session) {
		MallPersonalBasicDTO mpbDTO = (MallPersonalBasicDTO) session.getAttribute("MallPersonalBasicDTO");
		MallInsuredDetailDTO midDTO = (MallInsuredDetailDTO) session.getAttribute("MallInsuredDetailDTO");
		InsuranceApplyDto iaDto = (InsuranceApplyDto) session.getAttribute("init");
		Integer customerId = (Integer) session.getAttribute("customer_id");

		mallServiceTemp.completeContract(mpbDTO, midDTO, iaDto, customerId);

		sessionClear(session);
		
		return "redirect:/mall2/complete";
	}

    /*
     * AJAX: 프론트엔드로부터 결제 완료 콜백을 받아 실제 결제 기록과 대조하여 검증합니다.
     */
    @PostMapping("/payment/verify")
    @ResponseBody
    public boolean verifyPayment(@RequestBody PaymentCallbackRequest request) {
        try {
            return mallPaymentService.verifyPayment(request);
        } catch (Exception e) {
            System.err.println("결제 검증 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
    
    // 결제 검증 요청 시 프론트엔드에서 보낼 데이터를 담을 DTO
    public static class PaymentCallbackRequest {
        private String imp_uid;
        private String merchant_uid;

        public String getImp_uid() { return imp_uid; }
        public void setImp_uid(String imp_uid) { this.imp_uid = imp_uid; }
        public String getMerchant_uid() { return merchant_uid; }
        public void setMerchant_uid(String merchant_uid) { this.merchant_uid = merchant_uid; }
    }
	
	/*
	 * Step 6: 계약 완료 페이지
	 * 모든 절차가 완료된 후 사용자에게 보여주는 최종 안내 페이지입니다.
	 */
	@GetMapping("/complete")
	public String completeContract() {
		return "mall/contract_complete";
	}
	
	
    /**
     * 보험 계약 신청 과정에서 사용된 세션 속성들을 정리하는 private 헬퍼 메소드.
     * @param session 현재 HttpSession 객체
     */
	private void sessionClear(HttpSession session) {
		// 계약 플로우에서 사용된 DTO 정리
		session.removeAttribute("init");
		session.removeAttribute("MallPersonalBasicDTO");
		session.removeAttribute("MallInsuredDetailDTO");
		
		// 개별적으로 저장했던 속성들 정리
		session.removeAttribute("insured_name");
		session.removeAttribute("insured_phone_number");
		session.removeAttribute("insured_email");
		session.removeAttribute("customer_name");
		session.removeAttribute("is_smoker");
		session.removeAttribute("drinks");
		session.removeAttribute("driving_status");
		session.removeAttribute("medical_history");
		session.removeAttribute("medical_history_text");
	}
}