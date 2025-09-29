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
import com.kd.insuranceweb.mall.dto.MallInsuredDetailDTO;
import com.kd.insuranceweb.mall.dto.MallPersonalBasicDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mall")
@RequiredArgsConstructor
public class MallControllerTemp {
	
	private final MallServiceTemp mallServiceTemp;
	private final MallPaymentService mallPaymentService;
	private final PersonMapper personMapper;
	
	// 피보험자, 계약자 기본정보
	@GetMapping("/info")
	public String getPersonalBasic(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {
			if (loginUser != null) {
		        PersonDTO person = personMapper.selectById(loginUser.getPerson_id());
		        if (person != null) {
		            loginUser.setPerson(person);
		            model.addAttribute("loginUser", loginUser);
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
		return "redirect:/mall/detail";
	}
	
	// 피보험자 상세정보
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
		return "redirect:/mall/notice";
	}
	
	// 계약사항 알림
	@GetMapping("/notice")
	public String noticeContract(HttpSession session, Model model) {
		String insuredName = (String) session.getAttribute("insured_name");
		String customerName = (String) session.getAttribute("customer_name");
		String productName = (String) session.getAttribute("product_name");
		BigDecimal premium = (BigDecimal) session.getAttribute("premium");
		
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
		return "mall/notice_contract";
	}
	
	// 결제정보 등록
	@GetMapping("/payment")
	public String paymentForm() {
		return "mall/payment_form";
	}
	@PostMapping("/payment")
	public String paymentComplete(HttpSession session) {
		MallPersonalBasicDTO mpbDTO = (MallPersonalBasicDTO) session.getAttribute("MallPersonalBasicDTO");
		MallInsuredDetailDTO midDTO = (MallInsuredDetailDTO) session.getAttribute("MallInsuredDetailDTO");

		mallServiceTemp.insertPersonAndInsured(mpbDTO, midDTO);
		return "redirect:/mall/complete";
	}
    // 프론트엔드에서 결제 완료 후, '결제 검증'을 위해 호출되는 엔드포인트
    @PostMapping("/payment/verify")
    @ResponseBody // JSON 형태로 응답을 반환하기 위해 사용
    public boolean verifyPayment(@RequestBody PaymentCallbackRequest request) {
        try {
            // 서비스 로직을 호출해 imp_uid로 실제 결제된 금액을 가져오고,
            // 우리가 생성한 주문번호(merchant_uid)의 주문 금액과 비교하여 검증
        	
            return mallPaymentService.verifyPayment(request);
        } catch (Exception e) {
            // 예외 발생 시 콘솔에 로그를 남기고, 클라이언트에는 실패를 알림
            System.err.println("결제 검증 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
    // 결제 검증 요청 시 프론트엔드에서 보낼 데이터를 담을 DTO (Data Transfer Object)
    public static class PaymentCallbackRequest {
        private String imp_uid;
        private String merchant_uid;

        // Getter와 Setter
        public String getImp_uid() { return imp_uid; }
        public void setImp_uid(String imp_uid) { this.imp_uid = imp_uid; }
        public String getMerchant_uid() { return merchant_uid; }
        public void setMerchant_uid(String merchant_uid) { this.merchant_uid = merchant_uid; }
    }
	
	// 계약완료 안내페이지
	@GetMapping("/complete")
	public String completeContract() {
		return "mall/contract_complete";
	}
}
