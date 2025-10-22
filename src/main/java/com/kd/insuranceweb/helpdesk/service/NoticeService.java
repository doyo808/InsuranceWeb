package com.kd.insuranceweb.helpdesk.service;

import java.util.List;

import com.kd.insuranceweb.helpdesk.dto.NoticeDto;



public interface NoticeService {
	
	// 사용자용: 표시된 공지만 조회
	List<NoticeDto> getNoticeList(String keyword, int offset, int limit);
	
	// 메인페이지용: 최신 4개 공지 조회
    List<NoticeDto> getLatestNotices();
	
	// 관리자용: 전체 공지 조회
	List<NoticeDto> getAllNotices(String keyword, int offset, int limit);
	
	// 총 게시글수 반환 메서드 추가
	int getNoticeCount(String keyword);
	
	// 상세 조회 (사용자/관리자 공통)
	NoticeDto getNoticeDetail(Long notice_id);
	
	// 관리자용 CRUD
	void createNotice(NoticeDto notice);
	void updateNotice(NoticeDto notice);
	void deleteNotice(Long notice_id);
	
}
