package com.kd.insuranceweb.mypage;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.mypage.dto.ContractDto;
import com.kd.insuranceweb.mypage.dto.MarketingConsentDTO;

import lombok.RequiredArgsConstructor;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {
	
	private final MypageService mypageService;
	
	// 계약내용 확인
	@GetMapping("/MPDG0070")
	public String chkContracts(Model model) {
		List<ContractDto> dataList = mypageService.getAllContracts();
		List<ContractDto> dataListActive = mypageService.getActiveContracts();
        model.addAttribute("dataList", dataList);
        model.addAttribute("dataListActive", dataListActive);
		return "/mypage/chkContracts.html";
	}
	
	// 계약 상세정보
	@GetMapping("/MPDG0071/{id}")
	public String contractDetail(@PathVariable("id") Integer contract_id, Model model) {
		model.addAttribute("contract_id", contract_id);
		return "/mypage/contractDetail.html";
	}
	
	// 보험료 납입
	@GetMapping("/MPDG0080")
	public String payPremium() {
		return "/mypage/payPremium.html";
	}
	
	// 내 정보 확인/변경
	@GetMapping("/MPDG0093")
	public String openEditMyInfo(
			@AuthenticationPrincipal CustomUserDetails loginUser, 
			Model model) {
		
		loginUser = mypageService.getPersonAndCustomerInfo(loginUser);
		model.addAttribute("loginUser", loginUser);
		return "/mypage/EditMyInfo.html";
	}
	@PostMapping("/edit")
	public String doEditMyInfo(CustomerDTO customer) {
		mypageService.editPersonAndCustomer(customer);
		return "redirect:/mypage/MPDG0093"; // 수정 완료 후 다시 내 정보 페이지로 리다이렉트
	}
	
	// 마케팅 정보 활용 동의/철회 페이지 방문
	@GetMapping("/MPDG0247")
	public String viewMarketingAgreementPage(
			@AuthenticationPrincipal CustomUserDetails loginUser, 
			Model model) {
		
		MarketingConsentDTO dto = mypageService.getMarketingConsentDTO(loginUser.getCustomer_id());
		model.addAttribute("marketingConsentDTO", dto);
		System.out.println(dto);
		return "/mypage/marketingAgreement.html";
	}
	
	// 마케팅 정보 활용 동의/철회 업데이트
	@PostMapping("/marketing/update")
	public String updateConsentStatus(
			@AuthenticationPrincipal CustomUserDetails loginUser, 
			MarketingConsentDTO dto) {
		
		mypageService.saveOrUpdateMarketingConsent(loginUser.getCustomer_id(), dto);
		return "redirect:/home";
	}
}
