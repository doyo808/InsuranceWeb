package com.kd.insuranceweb.common.advice;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

	private final Environment env;
	
	@ModelAttribute("profile")
	public String addProfile() {
		String profile = env.acceptsProfiles(Profiles.of("dev")) ? "dev" : "prod"; 
		if (profile.equals("dev")) {
			System.out.println("!!!\n globalModelInfo에서 확인용:" + profile + "\n!!!");
		}
		return profile;
	}
}
