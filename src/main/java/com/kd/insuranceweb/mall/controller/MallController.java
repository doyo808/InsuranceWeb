package com.kd.insuranceweb.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mall")
@Controller
public class MallController {
	@GetMapping("/car/{id}")
	public String car(@PathVariable("id") String id) {
		if(id.equals("1")) {
			return "mall/intro/car/자동차보험인터넷";
		} else if(id.equals("2")) {
			return "mall/intro/car/자동차보험전화";
		} else if(id.equals("3")) {
			return "mall/intro/car/영업용자동차보험인터넷";
		} else if(id.equals("4")) {
			return "mall/intro/car/영업용자동차보험전화";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/ria/{id}")
	public String riaCar(@PathVariable("id") String id) {
		return "mall/calculate/car/car1";
	}
	
	@GetMapping("/car_discount/{id}")
	public String car_discount(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/car_discount/마일리지";
		} else if(id==2) {
			return "/mall/intro/car_discount/마일리지환급바로";
		} else if(id==3) {
			return "/mall/intro/car_discount/애니핏착한걷기";
		} else if(id==4) {
			return "/mall/intro/car_discount/Eco모빌리티";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/driver/{id}")
	public String driver(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/car_discount/운전자보험";
		} else if(id==2) {
			return "/mall/intro/car_discount/오토바이전용";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/health/{id}")
	public String health(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/health/실손의료비보험";
		} else if(id==2) {
			return "/mall/intro/health/암보험";
		} else if(id==3) {
			return "/mall/intro/health/치아보험";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/sick/{id}")
	public String sick(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/sick/유병자실손.html";
		} else if(id==2) {
			return "/mall/intro/sick/Smart유병자간편.html";
		} else {
			return "redirect:/home";
		}
	}
}
