package com.kd.insuranceweb.common.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.LoginUser;


@ControllerAdvice
public class GlobalUserInfoAdvice {
	
	@ModelAttribute("loginUser")
	public LoginUser addUserToModel(@AuthenticationPrincipal LoginUser userDetails) {
		System.out.println("globalUserInfo에서 확인용:" + userDetails);
        return userDetails;
    }
}
