package com.kd.insuranceweb.helpdesk.mapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;

import lombok.RequiredArgsConstructor;


@Repository
//Spring이 Bean으로 등록, DataAccessException 변환
@RequiredArgsConstructor
public class FaqMapper {
	
	private final SqlSessionTemplate sql;
	
	// 전체 페이징
	public List<FaqDto> findAllPage(int startRow, int endRow){
	    Map<String, Integer> params = new HashMap<>();
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);
	    return sql.selectList("FaqMapper.findAllPage", params);
	}
	
	// 카테고리별 페이징
	public List<FaqDto> findByCategoryPage(String category, int startRow, int endRow){
	    Map<String, Object> params = new HashMap<>();
	    params.put("category", category);
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);
	    return sql.selectList("FaqMapper.findByCategoryPage", params);
	}
	
	// 키워드 검색 페이징
	public List<FaqDto> searchByKeywordPage(String keyword, int startRow, int endRow){
	    Map<String, Object> params = new HashMap<>();
	    params.put("keyword", keyword);
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);
	    return sql.selectList("FaqMapper.searchByKeywordPage", params);
	}
	
	// 건수
	public int countAll() {
	    return sql.selectOne("FaqMapper.countAll");
	}

	public int countByCategory(String category){
	    return sql.selectOne("FaqMapper.countByCategory", category);
	}

	public int countByKeyword(String keyword){
	    return sql.selectOne("FaqMapper.countByKeyword", keyword);
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
