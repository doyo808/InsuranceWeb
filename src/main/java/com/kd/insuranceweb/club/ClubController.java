package com.kd.insuranceweb.club;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
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

	@GetMapping("/terms/point_terms.txt")
	public String showPointTerms(Model model) {

		String terms;
		try {
			 var resource = new ClassPathResource("static/club/terms/point_terms.txt");
		        terms = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

		} catch (IOException e) {
			terms = "약관을 불러오는 중 오류가 발생했습니다.";
			e.printStackTrace();
		}

		model.addAttribute("terms", terms);
		return "/club/terms/point_terms.txt";

	}

	@GetMapping("/VD.MPDG0295")
	public String anipoint() {
		return "/club/VD.MPDG0295";
	}

	@GetMapping("/PP060701_001")
	public String event() {
		return "/club/PP060701_001";
	}
}
