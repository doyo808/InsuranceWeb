package com.kd.insuranceweb.common.controller;

import java.util.List;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.kd.insuranceweb.helpdesk.dto.NoticeDto;
import com.kd.insuranceweb.helpdesk.service.NoticeService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class MainNoticeController {

    private final NoticeService noticeService;

    /**
     * 모든 페이지에서 footer_banner.html에서 사용 가능한 공지사항 4개
     */
    @ModelAttribute("mainNoticeList")
    public List<NoticeDto> mainNoticeList() {
        return noticeService.getLatestNotices();
    }
}
