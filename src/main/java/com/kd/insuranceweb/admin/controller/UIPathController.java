package com.kd.insuranceweb.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kd.insuranceweb.admin.service.UiPathService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UIPathController {
	private final UiPathService uiPathService;
	
	@GetMapping("/internal/notice/receive")
	public String openSecretForm() {
		return "admin/common/secretUiPathInput";
	}
	
	@PostMapping("/internal/notice/receive")
	@ResponseBody
	public String receiveNotice(@RequestParam String message, @RequestParam String key) {
	    if (!"superSecret123".equals(key)) {
	        return "Invalid key";
	    }
	    uiPathService.addNotice(message);
	    return "등록 완료";
	}
}
