package com.kd.insuranceweb.helpdesk.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
	private final SqlSessionTemplate sql;
	
	@Override
	public List<FaqDto> getAllFaqs() {		
		return sql.selectList("FaqMapper.findAll");
	}

	@Override
	public List<FaqDto> getFaqsByCategory(String category) {		
		return sql.selectList("FaqMapper.findByCategory", category);
	}

	@Override
	public List<FaqDto> searchFaqs(String keyword) {
		
		return sql.selectList("FaqMapper.searchByKeyword", keyword);
	}
	
	

}
