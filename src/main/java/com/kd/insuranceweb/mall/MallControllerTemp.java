package com.kd.insuranceweb.mall;

import java.math.BigDecimal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
