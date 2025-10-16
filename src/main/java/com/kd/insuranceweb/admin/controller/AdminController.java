package com.kd.insuranceweb.admin.controller;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kd.insuranceweb.admin.annotation.AdminActionLog;
import com.kd.insuranceweb.admin.dto.AdminActivityLogDTO;
import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;
import com.kd.insuranceweb.admin.dto.ContractDetailDTO;
import com.kd.insuranceweb.admin.dto.ContractListRowDTO;
import com.kd.insuranceweb.admin.dto.ContractSearchCriteria;
import com.kd.insuranceweb.admin.dto.ProductSearchCriteria;
import com.kd.insuranceweb.admin.service.AdminActivityService;
import com.kd.insuranceweb.admin.service.ClaimService;
import com.kd.insuranceweb.admin.service.ContractService;
import com.kd.insuranceweb.admin.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

	private final ClaimService claimService;
	private final ProductService productService;
	private final ContractService contractService;
	
	private final AdminActivityService activityService;
	
	// ===== 공통 페이지 =====
	@GetMapping("/login")
	public String login() {
		return "admin/common/login";
	}

	@GetMapping("/main")
	public String main(
			@ModelAttribute("criteria") ClaimSearchCriteria criteria, 
			Model model
	) {
		int claimsPendingCount = claimService.getPendingCount(criteria);
		model.addAttribute("claimsPendingCount", claimsPendingCount);
		int sellingCount = productService.countProductsOnSale();
		model.addAttribute("sellingCount", sellingCount); 
		int contractsPendingCount = contractService.getPendingCount();
	    model.addAttribute("contractsPendingCount", contractsPendingCount);
	    
	    List<AdminActivityLogDTO> recentActivities = activityService.getRecentActivities();
	    model.addAttribute("recentActivities", recentActivities);
	    
		return "admin/common/main";
	}

	// ===== 고객 문의 =====
	@GetMapping("/inquiry")
	public String inquiryList() {
		return "admin/inquiry/inquiryList";
	}

	@GetMapping("/inquiryAnswer")
	public String inquiryAnswer() {
		return "admin/inquiry/inquiryAnswer";
	}
	
	// ===== 계약 관리 =====
	@GetMapping("/contract")
	public String contractList(@ModelAttribute("criteria") ContractSearchCriteria criteria, Model model) {
		
		 // ★ 종료일 + 1일(배타 범위)
	    if (criteria.getTo() != null) {
	        criteria.setToExclusive(criteria.getTo().plusDays(1));
	    }
		
	    int totalCount = contractService.countReceipts(criteria);
	    List<ContractListRowDTO> rows = contractService.findReceiptsPage(criteria);

	    int pendingCount = contractService.getPendingCount();
	    model.addAttribute("pendingCount", pendingCount);
	    model.addAttribute("criteria", criteria);
	    model.addAttribute("contracts", rows);
	    model.addAttribute("totalCount", totalCount);
		return "admin/contract/contractList";
	}
	
	@GetMapping("/contractDetail")
	public String contractDetail(@RequestParam("contractId") Integer contractId, Model model) {
		ContractDetailDTO detail = contractService.getContractDetail(contractId);
		model.addAttribute("detail", detail);
		
	    return "admin/contract/contractDetail";

	}

	@PostMapping("/contract/{id}/approve")
	@AdminActionLog(type="CONTRACT", value="보험 계약 승인")
	public String approveContract(@PathVariable("id") Integer contractId,
	                              RedirectAttributes ra) {
	    contractService.approveContract(contractId);
	    ra.addFlashAttribute("toast", "계약이 승인되었습니다.");
	    return "redirect:/admin/contractDetail?contractId=" + contractId;
	}

	@PostMapping("/contract/{id}/reject")
	@AdminActionLog(type="CONTRACT", value="보험 계약 거절")
	public String rejectContract(@PathVariable("id") Integer contractId,
	                             @RequestParam("reason") String reason,
	                             RedirectAttributes ra) {
	    if (reason == null || reason.trim().isEmpty() || reason.length() > 500) {
	        ra.addFlashAttribute("error", "거절 사유는 1~500자 내로 입력해 주세요.");
	        return "redirect:/admin/contractDetail?contractId=" + contractId;
	    }
	    contractService.rejectContract(contractId, reason.trim());
	    ra.addFlashAttribute("toast", "거절 처리가 완료되었습니다.");
	    return "redirect:/admin/contractDetail?contractId=" + contractId;
	}


	
	
	// ===== 상품 관리 =====
	@GetMapping("/product")
	public String productList(@ModelAttribute("criteria") ProductSearchCriteria criteria, Model model) {
	    int totalCount   = productService.countProducts(criteria);
	    var products     = productService.findProductPage(criteria);
	    int sellingCount = productService.countProductsOnSale();

	    model.addAttribute("criteria", criteria);
	    model.addAttribute("products", products);
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("page",  criteria.getPage());
	    model.addAttribute("perPage", criteria.getSize());
	    model.addAttribute("sellingCount", sellingCount); 
	    return "admin/product/productList";
	    }
	
	@GetMapping("/product/new")
	public String productRegistration() {
		return "admin/product/productRegistration";
	}
	
	// ===== 보험금 청구 관리 =====
	@GetMapping("/claim")
	public String claimList(@ModelAttribute("criteria") ClaimSearchCriteria criteria, Model model) {
	    if (criteria.getTo() != null) {
	        criteria.setToExclusive(((LocalDate) criteria.getTo()).plusDays(1)); // [from, to+1) 범위
	    }
	    int pendingCount = claimService.getPendingCount(criteria);
	    int totalCount   = claimService.countClaims(criteria);
	    List<ClaimListRowDTO> claims = claimService.findClaimsPage(criteria);

	    model.addAttribute("pendingCount", pendingCount);
	    model.addAttribute("claims", claims);
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("page", criteria.getPage());
	    model.addAttribute("size", criteria.getSize());
	    return "admin/claim/claimList";
	}
	

	// 상세
	@GetMapping("/claimDetail")
	public String claimDetail(@RequestParam("claimId") Integer claimId, Model model) {
	    ClaimDetailDTO detail = claimService.getClaimDetail(claimId);
	    model.addAttribute("detail", detail);

	    model.addAttribute("detailFileName",  extractFileName(detail.getDetail_file_path()));
	    model.addAttribute("receiptFileName", extractFileName(detail.getReceipt_file_path()));
	    model.addAttribute("etcFileName",     extractFileName(detail.getEtc_file_path()));
	    return "admin/claim/claimDetail";
	}

	//승인
	@PostMapping("/claim/{claimId}/approve")
	@AdminActionLog(type="CLAIM", value="보험 청구 승인")
	public String approve(@PathVariable("claimId") Integer claimId, RedirectAttributes ra) {
		claimService.approveClaim(claimId);
		ra.addFlashAttribute("success", "청구가 승인되었습니다.");
		// 상세로 되돌아가려면 ID를 함께 리다이렉트!
		return "redirect:/admin/claimDetail?claimId=" + claimId;
	}

	//거절
	@PostMapping("/claim/{claimId}/reject")
	@AdminActionLog(type="CLAIM", value="보험 청구 거절")
	public String reject(@PathVariable("claimId") Integer claimId,
		    @RequestParam("reason") String reason,
		    RedirectAttributes ra
		) {
		    // 서버측 검증
		    if (reason == null || reason.trim().isEmpty() || reason.length() > 500) {
		        ra.addFlashAttribute("error", "거절 사유는 1~500자 내로 입력해 주세요.");
		        return "redirect:/admin/claimDetail?claimId=" + claimId;
		    }

		    claimService.rejectClaim(claimId, reason.trim());
		    ra.addFlashAttribute("toast", "거절 처리가 완료되었습니다.");
		    return "redirect:/admin/claimDetail?claimId=" + claimId;
		}
	
	private String extractFileName(String path) {
	    if (path == null || path.isBlank()) return null;
	    int slash = path.lastIndexOf('/');
	    int back  = path.lastIndexOf('\\');  // 윈도우 대비
	    int idx = Math.max(slash, back);
	    return (idx >= 0) ? path.substring(idx + 1) : path;
	}
	
	
	@GetMapping("/claim/{id}/file")
	public ResponseEntity<Resource> downloadClaimFile(
	        @PathVariable("id") Integer claimId,
	        @RequestParam("type") String type // detail | receipt | etc
	) throws Exception {

	    // 1) DB에서 해당 청구건의 파일 경로를 읽는다 (신뢰할 수 있는 소스만 사용)
	    ClaimDetailDTO dto = claimService.getClaimDetail(claimId);
	    if (dto == null) {
	        return ResponseEntity.notFound().build();
	    }

	    String filePath;
	    switch (type) {
	        case "detail":  filePath = dto.getDetail_file_path();  break;
	        case "receipt": filePath = dto.getReceipt_file_path(); break;
	        case "etc":     filePath = dto.getEtc_file_path();     break;
	        default:        return ResponseEntity.badRequest().build();
	    }
	    if (filePath == null || filePath.isBlank()) {
	        return ResponseEntity.notFound().build();
	    }

	    // 2) 경로 정규화 + (선택) 안전 체크
	    Path path = Paths.get(filePath).normalize().toAbsolutePath();

	    // 선택) 업로드 루트 경로를 지정했다면 루트 밖 접근 차단
	    // Path base = Paths.get("C:/javaweb_yhs/InsuranceWebUploadedFiles").toAbsolutePath().normalize();
	    // if (!path.startsWith(base)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

	    if (!Files.exists(path) || !Files.isReadable(path)) {
	        return ResponseEntity.notFound().build();
	    }

	    // 3) 리소스 스트리밍
	    Resource resource = new UrlResource(path.toUri());
	    if (!resource.exists()) {
	        return ResponseEntity.notFound().build();
	    }

	    // 4) Content-Type & 다운로드 파일명
	    String contentType = Files.probeContentType(path);
	    if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

	    String filename = path.getFileName().toString();
	    String encoded  = org.springframework.web.util.UriUtils.encode(filename, StandardCharsets.UTF_8);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
	        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(Files.size(path)))
	        .contentType(MediaType.parseMediaType(contentType))
	        .body(resource);
	}

}
