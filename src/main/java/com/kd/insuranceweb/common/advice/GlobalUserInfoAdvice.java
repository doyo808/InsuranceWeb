package com.kd.insuranceweb.common.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.kd.insuranceweb.common.dto.CustomUserDetails;

@ControllerAdvice
public class GlobalUserInfoAdvice {
	
	@ModelAttribute("loginUser")
	public CustomUserDetails addUserToModel(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails;
    }
}
