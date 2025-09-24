package com.kd.insuranceweb.common.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SecurityController {
	
	@GetMapping("/login")
	public String login() {
		return "common/login";
	}
	
	@GetMapping("/forceLogout")
	public String forceLogout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
	    return "redirect:/login?logout";
	}
	
	@GetMapping("/api/auth/status")
	@ResponseBody
	public ResponseEntity<?> checkLoginStatus(Authentication authentication) {
		boolean isLoggedIn = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
		return ResponseEntity.ok(Map.of("loggedIn", isLoggedIn));
	}
}
