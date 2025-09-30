package com.kd.insuranceweb.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestHomeController {
	
	@GetMapping(value = {"/test/home", "/test", "/test/index"}) 
	public String index() {
		return "/test/index";
	}
	
	// fragment 테스트
	@GetMapping("/test/sample") 
	public String sample() {
		return "/test/sample";
	}
	
	
	//@GetMapping("/helpdesk/link")
	//public String test() {
	//	return "/helpdesk/PP060801_001.html";
	//}
}
