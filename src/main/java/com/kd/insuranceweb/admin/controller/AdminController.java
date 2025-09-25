package com.kd.insuranceweb.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

	@GetMapping("/login")
	public String login () {
		return "admin/common/login";
	}
	
	@GetMapping("/main")
	public String main() {
		return "admin/common/main";
	}
	
	@GetMapping("/claimList") 
	public String claimList() {
		return "admin/claim/claimList";
	}
	
	@GetMapping("/claimDetail") 
	public String claimDetail() {
		return "admin/claim/claimDetail";
	}
	
}
