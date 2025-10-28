package com.kd.insuranceweb.admin.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
	
	private final AdminActivityService activityService;
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
	
	
	// 윤한식 ===== 고객가입후기 =====
	@GetMapping("/review")
	public String reviewListAdmin(
	        @RequestParam(value = "category", required = false) String category,
	        @RequestParam(value = "subCategory", required = false) String subCategory,
	        @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
	        @RequestParam(value = "author_name", required = false) String author_name,
	        @RequestParam(value = "is_visible", required = false) String is_visible,
	        @RequestParam(value = "from", required = false) String from,
	        @RequestParam(value = "to", required = false) String to,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	        Model model) {

	    int pageSize = 10;
	    int startRow = (page - 1) * pageSize + 1;
	    int endRow = page * pageSize;

	    Map<String, Object> params = new HashMap<>();
	    params.put("category", category);
	    params.put("subCategory", subCategory);
	    params.put("searchKeyword", searchKeyword);
	    params.put("author_name", author_name);
	    params.put("is_visible", is_visible);
	    params.put("from", from);
	    params.put("to", to);
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);

	    List<ReviewDto> reviewList = reviewService.getReviewListForAdmin(params);
	    int totalCount = reviewService.getReviewCountForAdmin(params);

	    model.addAttribute("reviewList", reviewList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("pageSize", pageSize);

	    // 검색 조건 유지
	    model.addAttribute("category", category);
	    model.addAttribute("subCategory", subCategory);
	    model.addAttribute("searchKeyword", searchKeyword);
	    model.addAttribute("author_name", author_name);
	    model.addAttribute("is_visible", is_visible);
	    model.addAttribute("from", from);
	    model.addAttribute("to", to);

	    return "admin/review/reviewList";
	}







	
	// 윤한식 ===== NOTICE =====
	@GetMapping("/notice")
	public String noticeList(
	        @RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "writer", required = false) String writer,
	        @RequestParam(value = "is_visible", required = false) String isVisible,
	        @RequestParam(value = "from", required = false) String from,
	        @RequestParam(value = "to", required = false) String to,
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        Model model) {

	    int pageSize = 10; // 한 페이지 표시 개수
	    int offset = (page - 1) * pageSize;	   

	    Map<String, Object> search = new HashMap<>();
	    search.put("keyword", keyword);
	    search.put("writer", writer);
	    search.put("is_visible", isVisible);
	    search.put("from", from);
	    search.put("to", to);
	    search.put("offset", offset);
	    search.put("limit", pageSize);

	    // 관리자 전용 메서드 호출
	    List<NoticeDto> noticeList = noticeService.getAdminNoticeList(search);
	    int totalCount = noticeService.getAdminNoticeCount(search);
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    // ★ 전체 row 기준 시작 번호 계산
	    int startNo = totalCount - offset;

	    model.addAttribute("noticeList", noticeList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPage", totalPage);
	    model.addAttribute("startNo", startNo); // ★ 추가
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("writer", writer);
	    model.addAttribute("is_visible", isVisible);
	    model.addAttribute("from", from);
	    model.addAttribute("to", to);

	    return "admin/notice/noticeList";
	}


	// 신규
	@GetMapping("/notice/new")
	public String newNotice(Model model, Principal principal) {
	    model.addAttribute("noticeDto", new NoticeDto());
	    model.addAttribute("isNew", true);
	    
	    // 로그인 아이디를 모델에 추가
	    model.addAttribute("loginId", principal.getName());
	    
	    return "admin/notice/noticeDetail";
	}


    // 상세/수정
	@GetMapping("/notice/detail/{id}")
	public String noticeDetail(@PathVariable("id") Long notice_id, Model model, Principal principal) {
	    NoticeDto notice = noticeService.getNoticeDetail(notice_id);
	    model.addAttribute("noticeDto", notice);
	    model.addAttribute("isNew", false);
	    
	    // 로그인 아이디를 모델에 추가
	    model.addAttribute("loginId", principal.getName());
	    
	    return "admin/notice/noticeDetail";
	}


    // 저장/수정
    @PostMapping("/notice/save")
    public String saveNotice(@ModelAttribute NoticeDto noticeDto) {
        if (noticeDto.getNotice_id() == null) {
            noticeService.createNotice(noticeDto);
        } else {
            noticeService.updateNotice(noticeDto);
        }
        return "redirect:/admin/notice";
    }

    // 삭제
    @PostMapping("/notice/{id}/delete")
    public String deleteNotice(@PathVariable("id") Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/admin/notice";
    }
	
	
	
	// 윤한식 ===== FAQ =====	
	@GetMapping("/faq")
    public String faqList(
            @RequestParam(value = "category", required = false, defaultValue = "") String category,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "writer", required = false, defaultValue = "") String writer,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {

        int pageSize = 10; 
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        List<FaqDto> faqList = faqService.getAdminFaqListPaged(category, keyword, writer, fromDate, toDate, startRow, endRow);

        int faqCount = faqService.getAdminFaqCount(category, keyword, writer, fromDate, toDate);
        int totalPages = (int) Math.ceil(faqCount / (double) pageSize);

        model.addAttribute("faqList", faqList);
        model.addAttribute("faqCount", faqCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("writer", writer);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "admin/faq/faqList";
    }
	
	//** 신규 등록 페이지 **/
	@GetMapping("/faq/new")
	public String newFaqForm(Model model, Principal principal) {
	    model.addAttribute("faq", new FaqDto());
	    model.addAttribute("isNew", true);

	    // 로그인 아이디를 작성자로 모델에 추가
	    if (principal != null) {
	        model.addAttribute("loginId", principal.getName());
	    } else {
	        model.addAttribute("loginId", ""); // 비로그인 시 기본값
	    }

	    return "admin/faq/faqDetail";
	}


    /** 상세 보기 (수정용) **/
    @GetMapping("/faq/{id}")
    public String getFaqDetail(@PathVariable("id") Long id, Model model) {
        FaqDto faq = faqService.getFaqById(id);
        model.addAttribute("faq", faq);
        model.addAttribute("isNew", false);
        return "admin/faq/faqDetail";
    }

    /** 저장 (신규 또는 수정 공용) **/
    @PostMapping("/faq/save")
    public String saveFaq(@ModelAttribute FaqDto faq) {
        if (faq.getFaq_id() == null) {
            faqService.insertFaq(faq);
        } else {
            faqService.updateFaq(faq);
        }
        return "redirect:/admin/faq";
    }
    
    
    /** 삭제 **/
    @PostMapping("/faq/{id}/delete")
    public String deleteFaq(@PathVariable("id") Long id) {
        faqService.deleteFaq(id);
        return "redirect:/admin/faq";
    }
    
    
 	// Summernote 이미지 업로드
    @PostMapping("/uploadImage")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        if (file.isEmpty()) {
            result.put("error", "파일이 없습니다.");
            return result;
        }

        try {
            // 1) 업로드 루트 폴더 (예: 프로젝트 외부, 서버 상의 안전한 위치)
            String uploadDir = "C:/javaweb_yhs/InsuranceWebUploadedFiles/faq_images/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // 2) 저장할 파일명 (UUID 또는 timestamp + 원본 이름)
            String originalFilename = file.getOriginalFilename();
            String filename = System.currentTimeMillis() + "_" + originalFilename;

            Path filepath = Paths.get(uploadDir, filename);
            Files.write(filepath, file.getBytes());

            // 3) 브라우저에서 접근할 수 있는 URL (컨텍스트 경로 기준)
            String fileUrl = "/faq/images/" + filename; // /faq/images/ 매핑 필요

            result.put("url", fileUrl);
            result.put("success", true);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("error", "파일 저장 실패");
        }

        return result;
    }

    // 업로드된 이미지를 서빙
    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> serveImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get("C:/javaweb_yhs/InsuranceWebUploadedFiles/faq_images/", filename);
        if (!Files.exists(path)) return ResponseEntity.notFound().build();
        byte[] bytes = Files.readAllBytes(path);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }
	
	

}
