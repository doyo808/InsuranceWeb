package com.kd.insuranceweb.club.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.club.dto.Card_benefit_interest_free;
import com.kd.insuranceweb.club.service.CardBenefitService;

import lombok.RequiredArgsConstructor;


@RequestMapping("/club")
@Controller
@RequiredArgsConstructor
public class ClubController {

	private final CardBenefitService cardBenefitService;
	
    @GetMapping("/PP050301_001")
    public String creditCardBenefit1(@RequestParam(name="tab", defaultValue="interest") String tab, Model model) {
		
    	String[] cards = {"삼성카드", "현대카드", "롯데카드", "KB국민카드", "신한카드"};
    	List<Card_benefit_interest_free> all = new ArrayList<>();
			
			for (String name : cards) {
				all.addAll(cardBenefitService.selectBenefitInterestFree(name));
			}
    	
    	model.addAttribute("benefitInterestFree", all);
    	
    	// 2) breadcrumb 라벨만 주입 (마지막 라벨은 탭/상태에 따라 변경 가능)
        String current = switch (tab) {
            case "interest-free" -> "무이자 할부";
            case "points"   -> "포인트 결제";
            case "post-charge"  -> "후청구 서비스";
            default         -> "무이자 할부";
        };
        model.addAttribute("bcLabels",
                java.util.List.of("혜택/서비스", "신용카드 혜택", current));
    	
        return "club/PP050301_001";
    }
    
	@GetMapping("/PP050401_001")
	public String creditCardBenefit2() {
		return "club/PP050401_001";
	}

	@GetMapping("/VD.MPDG0295")
	public String anipoint(Model model) {
		return "club/VD.MPDG0295";
	}

	@GetMapping("/PP060701_001")
	public String eventList() {
		return "club/PP060701_001";
	}

}
