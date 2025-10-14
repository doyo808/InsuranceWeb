package com.kd.insuranceweb.helpdesk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kd.insuranceweb.helpdesk.dto.NoticeDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
	private final SqlSessionTemplate sql;

	// 사용자용: 표시된 공지만 조회
	@Override
	public List<NoticeDto> getNoticeList(String keyword, int offset, int limit) {
		
		Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);
        return sql.selectList("NoticeMapper.selectNoticeList", params);
		
	}

	// 관리자용: 전체 공지 조회
	@Override
	public List<NoticeDto> getAllNotices(String keyword, int offset, int limit) {
		
		Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);
        return sql.selectList("NoticeMapper.selectAllNotices", params);
	}
	
	@Override
	public int getNoticeCount(String keyword) {
		Map<String, Object>params = new HashMap<>();
		params.put("keyword", keyword);
		return sql.selectOne("NoticeMapper.countNotices", params);
	}

	//상세조회
	@Override
	public NoticeDto getNoticeDetail(Long notice_id) {
		
		return sql.selectOne("NoticeMapper.selectNoticeDetail", notice_id);
	}

	@Override
	@Transactional
	public void createNotice(NoticeDto notice) {
		sql.insert("NoticeMapper.insertNotice", notice);
		
	}

	@Override
	@Transactional
	public void updateNotice(NoticeDto notice) {
		sql.update("NoticeMapper.updateNotice", notice);
		
	}

	@Override
	@Transactional
	public void deleteNotice(Long notice_id) {
		sql.delete("NoticeMapper.deleteNotice", notice_id);
		
	}

}
