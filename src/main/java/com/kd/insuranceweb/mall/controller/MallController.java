package com.kd.insuranceweb.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.mall.service.MallService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequestMapping("/mall")
@Controller
@RequiredArgsConstructor
public class MallController {
	
	private final MallService service;
	
	@GetMapping("/car/{id}")
	public String car(@PathVariable("id") String id) {
		if(id.equals("1")) {
			return "mall/intro/car/자동차보험인터넷";
		} else if(id.equals("2")) {
			return "mall/intro/car/자동차보험전화";
		} else if(id.equals("3")) {
			return "mall/intro/car/영업용자동차보험인터넷";
		} else if(id.equals("4")) {
			return "mall/intro/car/영업용자동차보험전화";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/ria/car/{id}")
	public String riaCar(@PathVariable("id") String id) {
		return "mall/calculate/car/inputForm";
	}
	@PostMapping("/ria/car/{id}")
	public String riaCarData(@PathVariable("id") String id, 
			HttpSession session, 
			HttpServletRequest request,
			Model model) {
		// 이곳에 들어오기전 대기 시간동안 띄워줄 로딩바를 inputForm에서 만들어둔다
		
		// 받은 정보를 임의로 세션에 저장해둠
		String birth = request.getParameter("birth");
		String gender = request.getParameter("gender");
//		String job = request.getParameter("job");
		session.setAttribute("birth", birth);
		session.setAttribute("gender", gender);
//		session.setAttribute("job", job);
		
		// 리스트 불러오기 TEST
//		service.getList();
		
		// 계산된 보험료를 받아옴
		// DB에서 연결을 하고 데이터를 검색하기때문에 오래 걸릴수도 있는 작업
		double premiumRate = service.getPremiumRate(Integer.parseInt(id), birth, gender);

		System.out.println(premiumRate);
		if(premiumRate < 0) {
			return "mall/calculate/notFound";
		}
		
		
		// 데이터를 모델에 따로 실어놓는다
		model.addAttribute("premium", (Double) premiumRate);
		
		return "mall/calculate/car/selectCover";
	}
	
	@GetMapping("/car_discount/{id}")
	public String car_discount(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/car_discount/마일리지";
		} else if(id==2) {
			return "/mall/intro/car_discount/마일리지환급바로";
		} else if(id==3) {
			return "/mall/intro/car_discount/애니핏착한걷기";
		} else if(id==4) {
			return "/mall/intro/car_discount/Eco모빌리티";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/driver/{id}")
	public String driver(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/driver/운전자보험";
		} else if(id==2) {
			return "/mall/intro/driver/오토바이전용";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/health/{id}")
	public String health(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/health/실손의료비보험";
		} else if(id==2) {
			return "/mall/intro/health/암보험";
		} else if(id==3) {
			return "/mall/intro/health/치아보험";
		} else {
			return "redirect:/home";
		}
	}
	
	@GetMapping("/sick/{id}")
	public String sick(@PathVariable("id") Long id) {
		if(id==1) {
			return "/mall/intro/sick/유병자실손.html";
		} else if(id==2) {
			return "/mall/intro/sick/Smart유병자간편.html";
		} else {
			return "redirect:/home";
		}
	}
}
