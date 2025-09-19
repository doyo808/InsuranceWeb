package com.kd.insuranceweb.mypage;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.CustomerMapper;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import lombok.RequiredArgsConstructor;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {
	
	private final MypageService mypageService;
	private final PersonMapper personMapper;
	private final CustomerMapper customerMapper;
	
	@GetMapping("/MPDG0070")
	public String chkContracts() {
		return "/mypage/chkContracts.html";
	}
	
	@GetMapping("/MPDG0080")
	public String payPremium() {
		return "/mypage/payPremium.html";
	}
	
	@GetMapping("/MPDG0093")
	public String openEditMyInfo(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {
		if (loginUser != null) {
			PersonDTO person = personMapper.selectById(loginUser.getPerson_id());
			CustomerDTO customer = customerMapper.selectByLoginId(loginUser.getUsername());

			if (person != null && customer != null) {
				loginUser.setPerson(person);
	            loginUser.setCustomer(customer);

	            model.addAttribute("loginUser", loginUser);
			}
		}
		
		return "/mypage/EditMyInfo.html";
	}
	
	@PostMapping("/edit")
	public String doEditMyInfo(CustomerDTO customer) {
		
        // TODO: 여기서 customer 객체를 서비스 레이어로 전달하여 DB 업데이트 로직 수행
		mypageService.EditPersonAndCustomer(customer);
		
		
		return "redirect:/mypage/MPDG0093"; // 수정 완료 후 다시 내 정보 페이지로 리다이렉트
	}
}
