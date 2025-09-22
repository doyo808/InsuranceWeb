package com.kd.insuranceweb.common.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());
			
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				model.addAttribute("errorMessage", "요청하신 페이지를 찾을 수 없습니다.");
				return "common/error/commonError";
			}
			
			if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				model.addAttribute("errorMessage", "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
				return "common/error/commonError";
			}
		}
		
		model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
		return "common/error/commonError";
	}
	
}
