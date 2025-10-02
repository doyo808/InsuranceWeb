package com.kd.insuranceweb.helpdesk.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;
import com.kd.insuranceweb.helpdesk.service.FaqService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/helpdesk")
public class FaqController {
	
	private final FaqService faqService;

    // GET: 페이지 전체 로드
    @GetMapping("/PP060101_001")
    public String faqList(@RequestParam(value="category", required=false, defaultValue="전체") String category,
                          @RequestParam(value="keyword", required=false) String keyword,
                          Model model) {

        List<FaqDto> faqs;

        if (keyword != null && !keyword.isEmpty()) {
            faqs = faqService.searchFaqs(keyword);
        } else if (!"전체".equals(category)) {
            faqs = faqService.getFaqsByCategory(category);
        } else {
            faqs = faqService.getAllFaqs();
        }

        model.addAttribute("faqs", faqs);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);

        return "helpdesk/PP060101_001";
    }

    // POST: AJAX 검색 및 카테고리 필터링
    @PostMapping("/PP060101_001/search")
    public String searchFaqs(@RequestParam(value="category", required=false, defaultValue="전체") String category,
                             @RequestParam(value="keyword", required=false) String keyword,
                             Model model) {

        List<FaqDto> faqs;

        if (keyword != null && !keyword.isEmpty()) {
            faqs = faqService.searchFaqs(keyword);
        } else if (!"전체".equals(category)) {
            faqs = faqService.getFaqsByCategory(category);
        } else {
            faqs = faqService.getAllFaqs();
        }

        model.addAttribute("faqs", faqs);

        // fragment만 반환
        return "helpdesk/PP060101_answer :: faqList";
    }
}
	
	
	
	




