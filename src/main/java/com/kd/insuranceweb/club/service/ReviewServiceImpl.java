package com.kd.insuranceweb.club.service;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.kd.insuranceweb.club.dto.ReviewDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final SqlSessionTemplate sql;

	@Override
	public List<ReviewDto> getReviewList(Map<String, Object> params) {
		
		return sql.selectList("ReviewMapper.selectReviewList", params);
	}

	@Override
	public int getReviewCount(Map<String, Object> params) {
		
		return sql.selectOne("ReviewMapper.selectReviewCount", params);
	}

	@Override
	public ReviewDto getReviewById(Long reviewId) {
		
		return sql.selectOne("ReviewMapper.selectReviewById", reviewId);
	}

	@Override
	public int insertReview(ReviewDto review) {
		
		return sql.insert("ReviewMapper.insertReview", review);
	}

	@Override
	public int updateReview(ReviewDto review) {
		
		return sql.update("ReviewMapper.updateReview", review);
	}

	@Override
	public int deleteReview(Long reviewId) {
		
		return sql.delete("ReviewMapper.deleteReview", reviewId);
	}

}
