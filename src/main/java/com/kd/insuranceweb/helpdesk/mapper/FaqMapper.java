package com.kd.insuranceweb.helpdesk.mapper;


import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;

import lombok.RequiredArgsConstructor;


@Repository
//Spring이 Bean으로 등록, DataAccessException 변환
@RequiredArgsConstructor
public class FaqMapper {
	
	private final SqlSessionTemplate sql;
	
	// 전체 조회
	public List<FaqDto> findAll(){
		return sql.selectList("FaqMapper.findAll");
	}
	
	// 카테고리 조회
	public List<FaqDto> findByCategory(String category){
		return sql.selectList("FaqMapper.findByCategory", category);
	}
	
	// 키워드 검색
	public List<FaqDto> searchByKeyword(String keyword){
		return sql.selectList("FaqMapper.searchByKeyword", keyword);
	}

	// ID 조회	
	public FaqDto findById(Long faqId){
		return sql.selectOne("FaqMapper.findById", faqId);
	}
	
	// 등록
	public int insertFaq(FaqDto faq) {
		return sql.insert("FaqMapper.insertFaq", faq);
	}
	
	// 수정
	public int updateFaq(FaqDto faq){
		return sql.update("FaqMapper.updateFaq", faq);
	}
	
	// 삭제
	public int deleteFaq(Long faqId) {
		return sql.delete("FaqMapper.deleteFaq", faqId);
	}
	
	
	
	
	
	
}
