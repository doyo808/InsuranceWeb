package com.kd.insuranceweb.mall;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mall")
@RequiredArgsConstructor
public class MallControllerTemp {
	
	private final PersonMapper personMapper;
	
	@GetMapping("/info")
	public String inputInsuredInfo(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {
			if (loginUser != null) {
		        PersonDTO person = personMapper.selectById(loginUser.getPerson_id());
		        if (person != null) {
		            loginUser.setPerson(person);
		            model.addAttribute("loginUser", loginUser);
		        }
		    }
		return "mall/insured_info";
	}
	@GetMapping("/detail")
	public String inputInsuredDetailInfo() {
		return "mall/insured_detail";
	}
	@GetMapping("/notice")
	public String noticeContract() {
	   
		return "mall/notice_contract";
	}
}
