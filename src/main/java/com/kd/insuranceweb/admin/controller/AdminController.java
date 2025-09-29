package com.kd.insuranceweb.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;
import com.kd.insuranceweb.admin.service.AdminClaimService;
import com.kd.insuranceweb.admin.service.ClaimService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

	private final AdminClaimService adminClaimService;
	private final ClaimService claimService;

	// ===== 공통 페이지 =====
	@GetMapping("/login")
	public String login() {
		return "admin/common/login";
	}

	@GetMapping("/main")
	public String main() {
		return "admin/common/main";
	}

	// ===== 고객 문의 =====
	@GetMapping("/inquiry")
	public String inquiryList() {
		return "admin/inquiry/inquiryList";
	}

	// ===== 계약 관리 =====
	@GetMapping("/contract")
	public String contractList() {
		return "admin/contract/contractList";
	}

	
	// ===== 상품 관리 =====
	@GetMapping("/product")
	public String productList() {
		return "admin/product/productList";
	}

	// ===== 보험금 청구 관리 =====
	// 목록: GET /admin/claims
	@GetMapping("/claim")
	public String claimList(@ModelAttribute("criteria") ClaimSearchCriteria criteria, Model model) {

	    // 종료일 배타 처리: toExclusive = to + 1
	    if (criteria.getTo() != null) {
	        criteria.setToExclusive(criteria.getTo().plusDays(1));
	    }
	    // 기본값 보정(안 넘어온 경우 대비)
	    if (criteria.getPage() <= 0) criteria.setPage(1);
	    if (criteria.getSize() <= 0) criteria.setSize(8);

	    int pendingCount = claimService.getPendingCount();
	    int totalCount   = adminClaimService.countClaims(criteria);       // ★ 추가
	    var claims       = adminClaimService.findClaimsPage(criteria);    // ★ 페이징 쿼리 사용

	    model.addAttribute("pendingCount", pendingCount);
	    model.addAttribute("claims", claims);
	    model.addAttribute("page",  criteria.getPage());  // ★ pagination에 바인딩
	    model.addAttribute("size",  criteria.getSize());
	    model.addAttribute("totalCount", totalCount);     // ★ pagination에 바인딩

	    return "admin/claim/claimList";
	}


	// 상세
	@GetMapping("/claimDetail")
	public String claimDetail(@RequestParam("claimId") Integer claimId, Model model) {
		ClaimDetailDTO detail = claimService.getClaimDetail(claimId);
		model.addAttribute("detail", detail);
		return "admin/claim/claimDetail";
	}

	//승인
	@PostMapping("/claim/{claimId}/approve")
	public String approve(@PathVariable("claimId") Integer claimId) {
		claimService.approve(claimId);
		// 상세로 되돌아가려면 ID를 함께 리다이렉트!
		return "redirect:/admin/claimDetail?claimId=" + claimId;
	}

	//거절
	@PostMapping("/claim/{claimId}/reject")
	public String reject(@PathVariable("claimId") Integer claimId, @RequestParam("reason") String reason) {
		claimService.reject(claimId, reason);
		return "redirect:/admin/claimDetail?claimId=" + claimId;
	}

}
