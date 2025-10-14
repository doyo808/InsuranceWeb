package com.kd.insuranceweb.helpdesk.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;
import com.kd.insuranceweb.helpdesk.service.FaqService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/helpdesk")
public class FaqController {

    private final FaqService faqService;
    private static final int PAGE_SIZE = 10;

    private List<FaqDto> fetchFaqs(String category, String keyword, int page) {
        int startRow = (page - 1) * PAGE_SIZE + 1;
        int endRow = page * PAGE_SIZE;

        if (keyword != null && !keyword.isEmpty()) {
            return faqService.searchFaqs(keyword, startRow, endRow);
        } else if (!"전체".equals(category)) {
            return faqService.getFaqsByCategory(category, startRow, endRow);
        } else {
            return faqService.getAllFaqs(startRow, endRow);
        }
    }

    private int fetchTotalCount(String category, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return faqService.countSearchFaqs(keyword);
        } else if (!"전체".equals(category)) {
            return faqService.countFaqsByCategory(category);
        } else {
            return faqService.countAllFaqs();
        }
    }

    @GetMapping("/PP060101_001")
    public String faqPage(
            @RequestParam(value="category", defaultValue="전체") String category,
            @RequestParam(value="keyword", required=false) String keyword,
            @RequestParam(value="page", defaultValue="1") int page,
            Model model
    ) {
        List<FaqDto> faqs = fetchFaqs(category, keyword, page);
        int totalCount = fetchTotalCount(category, keyword);
        int totalPages = (int) Math.ceil(totalCount / (double) PAGE_SIZE);

        model.addAttribute("faqs", faqs);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "helpdesk/PP060101_001";
    }

    @PostMapping("/PP060101_001/search")
    public String searchFaqs(
            @RequestParam(value="category", defaultValue="전체") String category,
            @RequestParam(value="keyword", required=false) String keyword,
            @RequestParam(value="page", defaultValue="1") int page,
            Model model
    ) {
        List<FaqDto> faqs = fetchFaqs(category, keyword, page);
        int totalCount = fetchTotalCount(category, keyword);
        int totalPages = (int) Math.ceil(totalCount / (double) PAGE_SIZE);

        model.addAttribute("faqs", faqs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "helpdesk/PP060101_answer :: faqList";
    }
}
