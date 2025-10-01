package com.kd.insuranceweb.club.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.club.dto.Card_benefit_interest_free;
import com.kd.insuranceweb.club.service.AnypointService;
import com.kd.insuranceweb.club.service.CardBenefitService;
import com.kd.insuranceweb.common.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RequestMapping("/club")
@Controller
@RequiredArgsConstructor
public class ClubController {

	private final CardBenefitService cardBenefitService;

	private final AnypointService anypointService;

	@GetMapping("/PP050301_001")
	public String creditCardBenefit1(
	        @RequestParam(name = "tab", defaultValue = "interest-free") String tab, // ← 통일
	        Model model) {

	    String[] cards = {"삼성카드","현대카드","롯데카드","KB국민카드","신한카드"};
	    List<Card_benefit_interest_free> all = new ArrayList<>();
	    for (String name : cards) {
	        all.addAll(cardBenefitService.selectBenefitInterestFree(name));
	    }
	    model.addAttribute("benefitInterestFree", all);

	    String current = switch (tab) {
	        case "interest-free"  -> "무이자 할부";   // 혹시 옛 키로 들어와도 커버
	        case "points"         -> "포인트 결제";
	        case "post-charge"    -> "후청구 서비스";
	        default               -> "무이자 할부";
	    };
	    model.addAttribute("bcLabels", java.util.List.of("혜택/서비스", "신용카드 혜택", current));
	    return "club/creditCardBenefits";
	}

	@GetMapping("/PP050401_001")
	public String creditCardBenefit2() {
		return "club/anyPointGuide";
	}

	@GetMapping("/VD.MPDG0295")
	public String pointHistory(
	        @AuthenticationPrincipal Object principal,
	        Model model,
	        @RequestParam(name="start", required=false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
	        @RequestParam(name="end", required=false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
	        @RequestParam(name="months", required=false) Integer months
	) {
	    if (principal == null || principal.equals("anonymousUser")) {
	        return "redirect:/login?redirect=/VD.MPDG0295";
	    }

	    Integer customerId;
	    if (principal instanceof CustomUserDetails cud) {
	        customerId = cud.getCustomer_id();
	    } else {
	        customerId = 6; // admin 등 데모
	    }

	    // 기본값 처리
	    if (months == null) months = 3;
	    LocalDate today = LocalDate.now();

	    if (months > 0) {
	        if (end == null) end = today;
	        start = end.minusMonths(months);
	    } else { // months == 0 : 직접입력
	        if (end == null) end = today;
	        if (start == null) start = end.minusMonths(3); // 안전 기본값
	    }

	    // 서비스 호출
	    long balance = anypointService.findBalance(customerId.longValue());
	    var txns     = anypointService.findTxns(customerId.longValue(), start, end);

	    // 모델 바인딩 (뷰의 라디오/날짜에 반영)
	    model.addAttribute("balance", balance);
	    model.addAttribute("txns", txns);
	    model.addAttribute("start", start);
	    model.addAttribute("end", end);
	    model.addAttribute("months", months);

	    return "club/anyPointHistory";
	}

	@GetMapping("/PP060701_001")
	public String eventList() {
		return "club/event";
	}

	// 이벤트 상세 페이지
	@GetMapping("/event/index")
	public String eventPage() {
		return "club/event/index";
	}
	
	@GetMapping("/event/analysis")
	public String analysisEvent() {
		return "club/event/aiAnalysis"; 
			
	}

}
