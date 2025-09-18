package com.kd.insuranceweb.club.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.club.service.CardCompanyService;

import lombok.RequiredArgsConstructor;


@RequestMapping("/club")
@Controller
@RequiredArgsConstructor
public class ClubController {

	private final CardCompanyService cardCompanyService;
	
    @GetMapping("/PP050301_001")
    public String creditCardBenefit1(Model model) {
		
    	String[] cards = {"삼성카드", "현대카드"};
    	String phone_number = cardCompanyService.selectPhoneByName("삼성카드");
    	model.addAttribute("phone_number", phone_number);
    	
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

	@GetMapping("/PP060701_001")
	public String eventList() {
		return "/club/PP060701_001";
	}

}
