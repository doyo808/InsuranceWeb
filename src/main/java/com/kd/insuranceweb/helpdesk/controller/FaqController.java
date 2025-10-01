package com.kd.insuranceweb.helpdesk.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	/**
     * 고객센터 ARS 안내 페이지
     * - menu: 초기 선택된 탭 (ars1~ars4)
     * - tabs: 하드코딩 탭 정보 (th:each 미사용 가능)
     */
    @GetMapping("/PP060200_000")
    public String helpdesk(
            Model model,
            @RequestParam(defaultValue="ars1") String menu
    ) {
        model.addAttribute("menu", menu);

        // 하드코딩 탭 정보 (th:each 없이도 사용 가능)
        List<Map<String, String>> tabs = List.of(
                Map.of("id","ars1"),
                Map.of("id","ars2"),
                Map.of("id","ars3"),
                Map.of("id","ars4")
        );
        model.addAttribute("tabs", tabs);

        return "helpdesk/PP060200_000"; // templates/helpdesk/PP060200_000.html
    }

}




