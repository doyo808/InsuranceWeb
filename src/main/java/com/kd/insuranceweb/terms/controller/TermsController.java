package com.kd.insuranceweb.terms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/terms")
public class TermsController {    
    
	// AJAX: 날짜별 약관 파일 불러오기
	@GetMapping("/content/{date:.+}") // .+ : - 포함한 모든 문자 허용
    public String getTermsContent(@PathVariable("date") String date, Model model) {
        // templates/terms/content/{date}.html
        return "terms/content/" + date;
    }
    
}