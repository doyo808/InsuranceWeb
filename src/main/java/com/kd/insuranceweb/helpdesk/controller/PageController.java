package com.kd.insuranceweb.helpdesk.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
	
	// URL -> 실제 HTML 파일 매핑
    private static final Map<String, String> helpdeskPages = new HashMap<>();
    private static final Map<String, String> termsPages = new HashMap<>();

    static {
        helpdeskPages.put("bookmark", "helpdesk/PP060801_001");
        helpdeskPages.put("install", "helpdesk/PP060301_002");
        
        termsPages.put("electfinterms", "terms/PU010401_001");
        
    }

    @GetMapping("/helpdesk/{page}")
    public String routeHelpdeskPage(@PathVariable("page") String page) {
        // Map에서 찾아서 반환, 없으면 기본 페이지로 이동
        return helpdeskPages.getOrDefault(page, "helpdesk/PP060801_001");
    }
    
    @GetMapping("/terms/{page}")
    public String routeTermsPage(@PathVariable("page") String page) {
        // /terms/content/**는 TermsController가 처리하므로 충돌 없음
        return termsPages.getOrDefault(page, "terms/PU010401_001");
    }

}
