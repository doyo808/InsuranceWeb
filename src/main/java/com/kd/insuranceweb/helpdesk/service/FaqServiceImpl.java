package com.kd.insuranceweb.helpdesk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final SqlSessionTemplate sql;

    @Override
    public List<FaqDto> getAllFaqs(int startRow, int endRow) {
        Map<String, Object> param = new HashMap<>();
        param.put("startRow", startRow);
        param.put("endRow", endRow);
        return sql.selectList("FaqMapper.findAllPage", param);
    }

    @Override
    public List<FaqDto> getFaqsByCategory(String category, int startRow, int endRow) {
        Map<String, Object> param = new HashMap<>();
        param.put("category", category);
        param.put("startRow", startRow);
        param.put("endRow", endRow);
        return sql.selectList("FaqMapper.findByCategoryPage", param);
    }

    @Override
    public List<FaqDto> searchFaqs(String keyword, int startRow, int endRow) {
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", keyword);
        param.put("startRow", startRow);
        param.put("endRow", endRow);
        return sql.selectList("FaqMapper.searchByKeywordPage", param);
    }

    @Override
    public int countAllFaqs() {
        return sql.selectOne("FaqMapper.countAll");
    }

    @Override
    public int countFaqsByCategory(String category) {
        return sql.selectOne("FaqMapper.countByCategory", category);
    }

    @Override
    public int countSearchFaqs(String keyword) {
        return sql.selectOne("FaqMapper.countByKeyword", keyword);
    }
}
