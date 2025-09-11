package com.kd.insuranceweb.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@GetMapping(value = {"/test/home", "/test", "/test/index"}) 
	public String index() {
		return "/test/index";
	}
	
	// fragment 테스트
	@GetMapping("/test/sample") 
	public String sample() {
		return "/test/sample";
	}
	
	// 스프링시큐리티 테스트
	@GetMapping("/test/forceLogout")
	public String forceLogout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
	    return "redirect:/login?logout";
	}
}
