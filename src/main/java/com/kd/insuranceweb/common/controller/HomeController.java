package com.kd.insuranceweb.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping(value= {"/", "/index", "/home"})
	public String home() {
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
