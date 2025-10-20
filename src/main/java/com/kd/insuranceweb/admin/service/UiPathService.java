package com.kd.insuranceweb.admin.service;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.dto.UiPathNoticeDto;
import com.kd.insuranceweb.admin.mapper.UiPathMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UiPathService {
	
	private final UiPathMapper mapper;
	
	public void addNotice(String message) {
		// TODO Auto-generated method stub
		// 여기서 db로 등록
		// 제목: ex> 2025-10-20 뉴스 요약본 / 내용: rpa결과값(form으로 입력받음)
		
	}

	public UiPathNoticeDto getRecentNotice() {
	    UiPathNoticeDto notice = mapper.SelectOne();
	    if (notice == null) {
	        notice = new UiPathNoticeDto();
	        notice.setContent("공지 없음");
	        notice.setCreated(java.time.LocalDateTime.now());
	    }
	    return notice;
	}

}
