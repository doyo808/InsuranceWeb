package com.kd.insuranceweb.common.advice;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.kd.insuranceweb.common.dto.LoginUser;

import lombok.RequiredArgsConstructor;


@ControllerAdvice
@RequiredArgsConstructor
public class GlobalUserInfoAdvice {
	
	private final Environment env;
	
	@ModelAttribute("loginUser")
	public LoginUser addUserToModel(@AuthenticationPrincipal LoginUser userDetails) {
		
		if (env.acceptsProfiles(Profiles.of("dev"))) {
			System.out.println("---\n globalUserInfo에서 확인용:" + userDetails + "\n---");
		}
        return userDetails;
    }
}
