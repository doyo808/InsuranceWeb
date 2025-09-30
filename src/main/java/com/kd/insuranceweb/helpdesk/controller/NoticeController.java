package com.kd.insuranceweb.helpdesk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kd.insuranceweb.helpdesk.dto.NoticeDto;
import com.kd.insuranceweb.helpdesk.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/helpdesk")
public class NoticeController {
	
	private final NoticeService noticeService;
	private static final int PAGE_SIZE = 10;
	
	
	// 👇 [신규 추가] 사용자용 리스트 HTML 페이지 반환
	@GetMapping("/PP060400_000")
    public String helpdeskNoticePage() {
        return "helpdesk/PP060400_000";  // 템플릿 경로 반환
    }
	
	
	
	
	// 1.사용자 : 표시된 공지 전체 조회
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getVisibleNotices(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page){
		
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
	
	// 2.사용자 : 검색	
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<List<NoticeDto>> searchNotices(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page){
		
		int offset = (page - 1) * PAGE_SIZE;
		List<NoticeDto> notices = noticeService.getNoticeList(keyword, offset, PAGE_SIZE);
		return ResponseEntity.ok(notices);
	}
	
	// 3.상세페이지	
	@GetMapping("/detail/{noticeId}")
	public String noticeDetail(@PathVariable Long noticeId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "keyword", required = false) String keyword,
			Model model) {
		NoticeDto notice = noticeService.getNoticeDetail(noticeId);
		model.addAttribute("notice", notice);
		model.addAttribute("page", page); // 현재 페이지 유지
		model.addAttribute("keyword", keyword);
		return "helpdesk/PP060400_001"; //상세 페이지 Thymeleaf
	}
	
	// 4.관리자: 전체 공지 조회(모두)
	@GetMapping("/admin/list")
	@ResponseBody
	public ResponseEntity<List<NoticeDto>> getAllNoticesAdmin(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page){
		
		int offset = (page - 1) * PAGE_SIZE;
		List<NoticeDto> notices = noticeService.getAllNotices(keyword, offset, PAGE_SIZE);
		return ResponseEntity.ok(notices);
	}
	
	// 5.관리자 : 공지등록
	@PostMapping("/admin")
	@ResponseBody
	public ResponseEntity<String> createNotice(@RequestBody NoticeDto notice){
		noticeService.createNotice(notice);
		return ResponseEntity.ok("등록 성공");
	}
	
	//6.관리자 : 공지 수정
	@PutMapping("/admin/{noticeId}")
	public ResponseEntity<String> updateNotice(@PathVariable Long noticeId, @RequestBody NoticeDto notice){
		notice.setNoticeId(noticeId);
		noticeService.updateNotice(notice);		
		return ResponseEntity.ok("수정 성공");
	}
	
	//7.관리자 : 공지 삭제
	@DeleteMapping("/admin/{noticeId}")
	@ResponseBody
	public ResponseEntity<String> deleteNotice(@PathVariable Long noticeId){
		noticeService.deleteNotice(noticeId);
		return ResponseEntity.ok("삭제 성공");
	}
	

}
