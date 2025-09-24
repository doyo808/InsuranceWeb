package com.kd.insuranceweb.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kd.insuranceweb.common.dto.SignupStep1DTO;
import com.kd.insuranceweb.common.dto.SignupStep2DTO;
import com.kd.insuranceweb.common.service.SignupService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

	private final SignupService signupService;

	// 이름, 주민등록번호, 전화번호, 이메일 <폼>
	@PostMapping("/step1")
	public String processSignupStep1(
			@Valid SignupStep1DTO dto,
			BindingResult br,
			HttpServletRequest request) {
		
		if (br.hasErrors()) {
			return "/common/signup";
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("signupDataStep1", dto);
		return "redirect:/signup/step2";
	}
	// 다음페이지로 이동
	@GetMapping("/step2")
	public String signupStep2(HttpSession session) {
		
		if (session.getAttribute("signupDataStep1") == null) {
			return "redirect:/signup";
		}
		return "/common/signup2";
	}
	// 로그인 아이디, 비밀번호, 마케팅정보동의 <폼>
	@PostMapping("/complete")
	public String signupComplete(SignupStep2DTO dto2, HttpSession session) {
		
		// 1단계를 거치지 않았다면 리다이렉트
		SignupStep1DTO dto = (SignupStep1DTO) session.getAttribute("signupDataStep1");
		if (dto == null) {
			return "redirect:/signup";
		}
		
		// 새로 생성된 고객id
		int newCustomerId = signupService.insertPersonAndCustomer(dto, dto2);

		// 세션초기화
		session.removeAttribute("signupDataStep1");
		return "/common/signup3";
	}
	

	/// 중복검사 섹션
	@ResponseBody
	@GetMapping("/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmailDuplication(@RequestParam("email") String email) {
        
        boolean isAvailable = signupService.isEmailAvailable(email);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        
        return ResponseEntity.ok(response);
    }
	@ResponseBody
	@GetMapping("/check-login_id")
	public ResponseEntity<Map<String, Boolean>> checkloginIdDuplication(@RequestParam("login_id") String login_id) {
        
        boolean isAvailable = signupService.isloginIdAvailable(login_id);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }
}
