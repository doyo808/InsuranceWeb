package com.kd.insuranceweb.club;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/club")
@Controller
public class ClubController {
	
	@GetMapping("/PP050301_001")
	public String creditCardBenefit1() {
		return "/club/PP050301_001";
	}
	
	@GetMapping("/PP050401_001")
	public String creditCardBenefit2() {
		return "/club/PP050401_001";
				
	}
	
	@GetMapping("/VD.MPDG0295")
	public String anipoint() {
		return "/club/VD.MPDG0295";
	}
}
