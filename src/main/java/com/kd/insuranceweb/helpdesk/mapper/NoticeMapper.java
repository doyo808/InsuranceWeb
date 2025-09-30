package com.kd.insuranceweb.helpdesk.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kd.insuranceweb.helpdesk.dto.NoticeDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoticeMapper {
	private final SqlSessionTemplate sql;	
	
	// 공지사항 목록 조회(페이징 포함)
	public List<NoticeDto> selectNoticeList(String keyword, int offset, int limit){
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", keyword);
		params.put("offset", offset);
		params.put("limit", limit);
		return sql.selectList("NoticeMapper.selectNoticeList", params);
	}
	
	// 공지사항 총 개수 조회(페이지용)
	public int countNotices(String keyword) {
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", keyword);
		return sql.selectOne("NoticeMapper.countNotices", params); 
	}
	
//	// 관리자: 전체 공지 조회 (페이징용)
//	public List<NoticeDto> selectAllNotices(String keyword, int offset, int limit){
//		Map<String, Object> params = new HashMap<>();
//		params.put("keyword", keyword);
//		params.put("offset", offset);
//		params.put("limit", limit);
//		return sql.selectList("NoticeMapper.selectAllNotices", params);
//	}
	

	
	// ID별 공지 조회
	public NoticeDto selectNoticeDetail(Long noticeId) {
		return sql.selectOne("NoticeMapper.selectNoticeDetail", noticeId);
	}
	
	// 입력
	public void insertNotice(NoticeDto notice) {
		sql.insert("NoticeMapper.insertNotice", notice);
	}
	
	// 수정
	public void updateNotice(NoticeDto notice) {
		sql.update("NoticeMapper.updateNotice", notice);
	}
	
	// 삭제
	public void deleteNotice(Long noticeId) {
		sql.delete("NoticeMapper.deleteNotice", noticeId);
	}
	
	
	
	
	

}
