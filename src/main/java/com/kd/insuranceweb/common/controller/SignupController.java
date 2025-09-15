package com.kd.insuranceweb.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kd.insuranceweb.common.service.SignupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

	private final SignupService signupService;
	
	@GetMapping("/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmailDuplication(@RequestParam("email") String email) {
        
        boolean isAvailable = signupService.isEmailAvailable(email);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        
        return ResponseEntity.ok(response);
    }
}
