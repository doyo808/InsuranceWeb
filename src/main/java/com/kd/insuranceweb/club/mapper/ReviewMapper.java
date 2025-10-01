package com.kd.insuranceweb.club.mapper;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kd.insuranceweb.club.dto.ReviewDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewMapper {
	
	private final SqlSessionTemplate sql;
	
	// 후기 목록 조회 (탭, 검색, 페이징)
	public List<ReviewDto> selectReviewList(Map<String, Object> params){
		return sql.selectList("ReviewMapper.selectReviewList", params);
	}
	
	// 후기 개수 조회(페이지 total count)
	public int selectReviewCount(Map<String, Object> params) {
		return sql.selectOne("ReviewMapper.selectReviewCount", params);
	}
	
	// 후기 단건 조회
	public ReviewDto selectReviewById(Long reviewId) {
		return sql.selectOne("ReviewMapper.selectReviewById", reviewId);
	}
	
	
	// 후기 등록
	public int insertReview(ReviewDto review) {
		return sql.insert("ReviewMapper.insertReview", review);
	}
	
	
	// 후기 수정
	public int updateReview(ReviewDto review) {
		return sql.update("ReviewMapper.updateReview", review);
	}
	
	
	// 후기 삭제
	public int deleteReview(Long reviewId) {
		return sql.delete("ReviewMapper.deleteReview", reviewId);
	}	

}
