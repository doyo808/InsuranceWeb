package com.kd.insuranceweb.helpdesk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kd.insuranceweb.helpdesk.dto.NoticeDto;
import com.kd.insuranceweb.helpdesk.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/helpdesk")
public class NoticeController {

    private final NoticeService noticeService;
    private static final int PAGE_SIZE = 10;
    
    // 사용자용 리스트 HTML 페이지 반환
    @GetMapping("/PP060400_000")
    public String helpdeskNoticePage() {
        return "helpdesk/PP060400_000"; // 템플릿 경로 반환
    }    

    // 1. 사용자 : 표시된 공지 전체 조회
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getVisibleNotices(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        int offset = (page - 1) * PAGE_SIZE;
        List<NoticeDto> notices = noticeService.getNoticeList(keyword, offset, PAGE_SIZE);
        int totalCount = noticeService.getNoticeCount(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("notices", notices);
        result.put("totalCount", totalCount);
        result.put("currentPage", page);
        result.put("pageSize", PAGE_SIZE);

        return ResponseEntity.ok(result);
    }

    // 2. 사용자 : 검색
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<NoticeDto>> searchNotices(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        int offset = (page - 1) * PAGE_SIZE;
        List<NoticeDto> notices = noticeService.getNoticeList(keyword, offset, PAGE_SIZE);
        return ResponseEntity.ok(notices);
    }

    // 3. 상세페이지
    @GetMapping("/detail/{notice_id}")
    public String noticeDetail(
            @PathVariable("notice_id") Long noticeId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        NoticeDto notice = noticeService.getNoticeDetail(noticeId);
        model.addAttribute("notice", notice);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "helpdesk/PP060400_001";
    }
    
}
