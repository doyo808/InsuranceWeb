package com.kd.insuranceweb.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping(value = {"/test/home", "/test", "/test/index"}) 
	public String index() {
		return "/test/index";
	}
	
	@GetMapping("/test/sample") 
	public String sample() {
		return "/test/sample";
	}
}
