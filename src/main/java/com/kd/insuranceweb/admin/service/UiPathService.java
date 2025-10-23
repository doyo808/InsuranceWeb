package com.kd.insuranceweb.admin.service;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.dto.UiPathNoticeDto;
import com.kd.insuranceweb.admin.mapper.UiPathMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UiPathService {
	
	private final UiPathMapper mapper;
	
	public int addNotice(String content) {
		return mapper.insert(content);
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
