package com.kd.insuranceweb.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequestMapping("/mall")
@Controller
@RequiredArgsConstructor
public class MallController {
	
//	private final MallService service;
	
	// 가져온 값중 상품 id로 컨트롤러에서 구분해 각자의 html템플릿으로 연결시켜준다
	
	// 단순히 mall/상품번호(or이름)으로 통일한다
	
	@GetMapping("/{pageNum}")
	public String product(@PathVariable("pageNum") String pageNum) {
		
		return "/mall/products/"+pageNum;
	}
	
	@GetMapping("/ria/{id}")
	public String insuranceJoin(@PathVariable("id") String id, HttpSession session) {
		session.setAttribute("productId", id);
		return "/mall/calculate/selectCover";
	}
}
