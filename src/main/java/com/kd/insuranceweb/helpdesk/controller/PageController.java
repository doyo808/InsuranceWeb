package com.kd.insuranceweb.helpdesk.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
	
	// URL -> 실제 HTML 파일 매핑
    private static final Map<String, String> helpdeskPages = new HashMap<>();
    private static final Map<String, String> termsPages = new HashMap<>();

    static {
    	helpdeskPages.put("customerservice", "helpdesk/PP060200_000");    	
    	helpdeskPages.put("bookmark", "helpdesk/PP060801_001");
        helpdeskPages.put("install", "helpdesk/PP060301_002");
        
        termsPages.put("electfinterms", "terms/PU010401_001");
        termsPages.put("directterms", "terms/PU010401_011");
        termsPages.put("efprecaution", "terms/PU010401_002");
        
    }

    // 최초 helpdesk 페이지 SSR
    @GetMapping("/helpdesk/customerservice")
    public String helpdesk(
            @RequestParam(value = "menu", defaultValue = "ars1") String menu,
            Model model
    ) {
        model.addAttribute("menu", menu);
        return "helpdesk/PP060200_000"; // 기본 페이지
    }

    // helpdesk 내부 페이지 라우팅 (Map 기반)
    @GetMapping("/helpdesk/{page}")
    public String routeHelpdeskPage(
            @PathVariable("page") String page,
            @RequestParam(value = "menu", defaultValue = "ars1") String menu,
            Model model
    ) {
        model.addAttribute("menu", menu);
        // map에서 찾고 없으면 기본 페이지 반환
        return helpdeskPages.getOrDefault(page, "helpdesk/PP060200_000");
    }

    // terms 페이지 라우팅
    @GetMapping("/terms/{page}")
    public String routeTermsPage(@PathVariable("page") String page) {
        return termsPages.getOrDefault(page, "terms/PU010401_001");
    }

}
