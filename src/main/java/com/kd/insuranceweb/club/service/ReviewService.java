package com.kd.insuranceweb.club.service;

import java.util.List;
import java.util.Map;

import com.kd.insuranceweb.club.dto.ReviewDto;



public interface ReviewService {
	
	// 후기 목록 조회(탭, 검색, 페이징)
	List<ReviewDto> getReviewList(Map<String, Object> params);
	
	// 후기 총 개주 조회(페이징용)
	int getReviewCount(Map<String, Object> params);
	
	// 후기 단건 조회
	ReviewDto getReviewById(Long reviewId);
	
	// 후기 등록
    int insertReview(ReviewDto review);

    // 후기 수정
    int updateReview(ReviewDto review);

    // 후기 삭제
    int deleteReview(Long reviewId);
	

}
