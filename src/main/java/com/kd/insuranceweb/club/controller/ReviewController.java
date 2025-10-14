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
            @RequestParam(name="category", defaultValue = "자동차보험") String category,   // 탭 기본값
            @RequestParam(name="subCategory", required = false) String subCategory,         // 세부분류
            @RequestParam(name="searchType", required = false) String searchType,           // 내용/작성자
            @RequestParam(name="searchKeyword", required = false) String searchKeyword,
            @RequestParam(name="page", defaultValue = "1") int page,
            Model model) {

        // ---------------------------
        // 1. Null 기본값 처리
        // ---------------------------
        if(subCategory == null || subCategory.isBlank()) subCategory = "전체";
        if(searchType == null || searchType.isBlank()) searchType = "";
        if(searchKeyword == null || searchKeyword.isBlank()) searchKeyword = "";

        // ---------------------------
        // 2. 페이징 계산
        // ---------------------------
        int pageSize = 10;
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        // ---------------------------
        // 3. 파라미터 Map 생성
        // ---------------------------
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        // "전체"는 DB 조건에서 제외
        if(!"전체".equals(subCategory)) {
            params.put("subCategory", subCategory);
        }
        if(!searchType.isEmpty() && !searchKeyword.isEmpty()) {
            params.put("searchType", searchType);
            params.put("searchKeyword", searchKeyword);
        }
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        // ---------------------------
        // 4. DB 호출
        // ---------------------------
        List<ReviewDto> reviews = reviewService.getReviewList(params);
        int totalCount = reviewService.getReviewCount(params);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // ---------------------------
        // 5. 서브카테고리 리스트
        // ---------------------------
        List<String> subCategories;
        if("자동차보험".equals(category)) {
            subCategories = List.of("전체", "자동차(개인)", "자동차(법인)");
        } else {
            subCategories = List.of("전체", "운전자보험", "주택화재보험");
        }

        // ---------------------------
        // 6. Model에 데이터 전달
        // ---------------------------
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("subCategories", subCategories);

        return "club/PP050101_001";  // Thymeleaf template 위치
    }
}
