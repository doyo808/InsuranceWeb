package com.kd.insuranceweb.common.advice;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationException(MethodArgumentNotValidException ex, Model model) {
	    List<String> errors = ex.getBindingResult()
	                            .getAllErrors()
	                            .stream()
	                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
	                            .toList();

	    model.addAttribute("validationErrors", errors);
	    return "common/error/commonError";
	}
	
	
	@ExceptionHandler(Exception.class)
	public String handleAllUncaughtException(Exception ex, Model model) {
		log.error("오류발생: {}", ex.getMessage(), ex);
		model.addAttribute("errorMessage", "서비스 이용에 불편을 드려 죄송합니다. 문제가 지속되면 관리자에게 문의해주세요.");
		return "common/error/commonError";
	}
	
}
