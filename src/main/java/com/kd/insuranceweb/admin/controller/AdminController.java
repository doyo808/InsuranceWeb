package com.kd.insuranceweb.admin.controller;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;
import com.kd.insuranceweb.admin.dto.ContractListRowDTO;
import com.kd.insuranceweb.admin.dto.ContractSearchCriteria;
import com.kd.insuranceweb.admin.dto.ProductSearchCriteria;
import com.kd.insuranceweb.admin.service.ClaimService;
import com.kd.insuranceweb.admin.service.ContractService;
import com.kd.insuranceweb.admin.service.ProductService;
import com.kd.insuranceweb.club.dto.ReviewDto;
import com.kd.insuranceweb.club.service.ReviewService;
import com.kd.insuranceweb.helpdesk.dto.FaqDto;
import com.kd.insuranceweb.helpdesk.dto.NoticeDto;
import com.kd.insuranceweb.helpdesk.service.FaqService;
import com.kd.insuranceweb.helpdesk.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

	private final ClaimService claimService;
	private final ProductService productService;
	private final ContractService contractService;
	
	//윤한식 추가 Controller
	private final NoticeService noticeService;
	private final FaqService faqService;
	private final ReviewService reviewService;
	
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
		int pendingCount = claimService.getPendingCount(criteria);
		model.addAttribute("pendingCount", pendingCount);
		int sellingCount = productService.countProductsOnSale();
		model.addAttribute("sellingCount", sellingCount); 
		
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

	    model.addAttribute("criteria", criteria);
	    model.addAttribute("contracts", rows);
	    model.addAttribute("totalCount", totalCount);
		return "admin/contract/contractList";
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
	public String approve(@PathVariable("claimId") Integer claimId) {
		claimService.approveClaim(claimId);
		// 상세로 되돌아가려면 ID를 함께 리다이렉트!
		return "redirect:/admin/claimDetail?claimId=" + claimId;
	}

	//거절
	@PostMapping("/claim/{claimId}/reject")
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
	
	
	// 윤한식 ===== 고객가입후기 =====
	@GetMapping("/review")
	public String reviewList(
	        @RequestParam(value = "category", required = false) String category,
	        @RequestParam(value = "searchType", required = false) String searchType,
	        @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
	        Model model) {
	
	    Map<String, Object> params = new HashMap<>();
	    params.put("category", category);
	    params.put("searchType", searchType);
	    params.put("searchKeyword", searchKeyword);
	    // 페이징 없이 전체 표시 (필요 시 startRow, endRow 추가)
	    params.put("startRow", 1);
	    params.put("endRow", 1000);
	
	    List<ReviewDto> reviewList = reviewService.getReviewList(params);
	    model.addAttribute("reviewList", reviewList);
	
	    return "admin/review/reviewList";
	}
	
	// 윤한식 ===== NOTICE =====
	@GetMapping("/notice")
	public String noticeList(Model model) {
		// DB에서 전체 공지 조회 (관리자용)
		List<NoticeDto> noticeList = noticeService.getAllNotices(null, 0, 100);
        model.addAttribute("noticeList", noticeList);
        return "admin/notice/noticeList";
	}
	
	// 윤한식 ===== FAQ =====
	@GetMapping("/faq")
	public String faqList(Model model) {
        int startRow = 1;
        int endRow = 20; // 예시로 20개 표시
        List<FaqDto> faqList = faqService.getAllFaqs(startRow, endRow);
        model.addAttribute("faqList", faqList);
        return "admin/faq/faqList";
    }
	
	
	

}
