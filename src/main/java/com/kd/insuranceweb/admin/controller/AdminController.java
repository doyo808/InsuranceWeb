package com.kd.insuranceweb.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.service.AdminClaimService;
import com.kd.insuranceweb.admin.service.ClaimService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
	
	 private final AdminClaimService adminClaimService;
	 private final ClaimService claimService;
	 
	@GetMapping("/login")
	public String login () {
		return "admin/common/login";
	}
	
	@GetMapping("/main")
	public String main() {
		return "admin/common/main";
	}
	
	 @GetMapping("/claimList")
	    public String claimList(Model model) {
	        List<ClaimListRowDTO> rows = adminClaimService.getClaimList();
	        model.addAttribute("claims", rows);
	        return "admin/claim/claimList";
	        
	        // 상세보기로 이동할 때 사용할 라우트 예시
		    // @GetMapping("/admin/claimDetail/{id}")
		    // public String claimDetail(@PathVariable Long id, Model model) { ... }
	    }
	 
	 @GetMapping("/claimDetail/{claim_id}")
	 public String detail(@RequestParam("claim_id") Integer claimId, Model model) {
	    ClaimDetailDTO detail = claimService.getClaimDetail(claimId);
	    model.addAttribute("detail", detail);
	    return "admin/claim/claimDetail"; // 템플릿 경로에 맞게
	  }

	  @PostMapping("/{claimId}/approve")
	  public String approve(@PathVariable Integer claimId) {
	    claimService.approve(claimId);
	    return "redirect:/admin/claims/" + claimId;
	  }

	  @PostMapping("/{claimId}/reject")
	  public String reject(@PathVariable Integer claimId, @RequestParam String reason) {
	    claimService.reject(claimId, reason);
	    return "redirect:/admin/claims/" + claimId;
	  }
	
	
	  @GetMapping("/inquiryList")
		 public String inquiryList() {
			 return "admin/inquiry/inquiryList";
		 }
	  
	
	  @GetMapping("/contractList")
	  public String contractList() {
		  return "admin/contract/contractList";
	  }
	  
	  @GetMapping("/productList")
	  public String productList() {
		  return "admin/product/productList";
	  }
}
