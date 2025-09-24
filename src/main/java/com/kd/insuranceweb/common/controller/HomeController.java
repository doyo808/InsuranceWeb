package com.kd.insuranceweb.common.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final PersonMapper personMapper;
	
	@GetMapping(value= {"/", "/index", "/home"})
	public String home(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {
		if (loginUser != null) {
			PersonDTO person = personMapper.selectById(loginUser.getPerson_id());
			if (person != null) {
				model.addAttribute("person_name", person.getPerson_name());
			}
		}
		return "index";
	}
	@GetMapping("/signup")
	public String signup() {
		return "/common/signup";
	}
//	레이아웃 확인용 매핑
//	@GetMapping("/signup/complete")
//	public String signupcom() {
//		return "/common/signup3";
//	}
}
