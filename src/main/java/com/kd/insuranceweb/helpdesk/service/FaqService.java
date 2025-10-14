package com.kd.insuranceweb.helpdesk.service;

import java.util.List;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;



public interface FaqService {
	
	// 전체 FAQ 조회 (페이징 적용)
	List<FaqDto> getAllFaqs(int startRow, int endRow);

    // 카테고리별 조회 (페이징 적용)
    List<FaqDto> getFaqsByCategory(String category, int startRow, int endRow);

    // 키워드 검색 (페이징 적용)
    List<FaqDto> searchFaqs(String keyword, int starRow, int endRow);

    // 전체 FAQ 개수 (페이징용)
    int countAllFaqs();

    // 카테고리별 FAQ 개수 (페이징용)
    int countFaqsByCategory(String category);

    // 키워드 검색 FAQ 개수 (페이징용)
    int countSearchFaqs(String keyword);

}
