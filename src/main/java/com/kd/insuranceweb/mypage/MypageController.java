package com.kd.insuranceweb.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mypage")
@Controller
public class MypageController {
	
	@GetMapping("/MPDG0070")
	public String chkContracts() {
		return "/mypage/chkContracts.html";
	}
	
	@GetMapping("/MPDG0080")
	public String payPremium() {
		return "/mypage/payPremium.html";
	}
}
