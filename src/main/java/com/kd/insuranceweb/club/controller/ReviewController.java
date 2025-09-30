package com.kd.insuranceweb.club.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.club.dto.ReviewDto;
import com.kd.insuranceweb.club.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/club")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/PP050101_001")
    public String reviewList(
            @RequestParam(defaultValue = "자동차보험") String category,  // 탭 기본값
            @RequestParam(required = false) String subCategory,          // 세부분류
            @RequestParam(required = false) String searchType,           // 내용/작성자
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        int pageSize = 10;
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("subCategory", subCategory);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        List<ReviewDto> reviews = reviewService.getReviewList(params);
        int totalCount = reviewService.getReviewCount(params);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // 세부분류 리스트
        List<String> subCategories;
        if("자동차보험".equals(category)) {
            subCategories = List.of("전체", "자동차(개인)", "자동차(법인)");
        } else {
            subCategories = List.of("전체", "운전자보험", "주택화재보험");
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("subCategories", subCategories); // 모델에 전달

        return "club/PP050101_001";  // Thymeleaf template 위치
    }
}
