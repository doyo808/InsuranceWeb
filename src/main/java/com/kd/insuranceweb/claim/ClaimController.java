package com.kd.insuranceweb.claim;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kd.insuranceweb.claim.dto.Claim;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimController {
    
    @Autowired
    private ClaimService claimService;
    
    private final ClaimMapper claimMapper;
    
	private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
		    "application/pdf",
		    "image/png",
		    "image/jpeg"
		);
	
	@GetMapping("/claimpage1")
	public String claimPage1() {
		
		return "claim/claimpage1";
	}
	
	@GetMapping("/claimpage2")
	public String claimPage2() {
		
		return "/claim/claimpage2";
	}
	
	@PostMapping("/claimpage3")
	public String claimPage3(
			@RequestParam("target") String target,
	        @RequestParam("type") String type,
	        HttpSession session,
	        Model model){

    // ✅ 세션에 저장
    session.setAttribute("target", target);
    session.setAttribute("type", type);

    // ✅ 세션 만료시간 30분(1800초) 설정
    session.setMaxInactiveInterval(30 * 60);

    // 이후 페이지에서 model로도 확인 가능
    model.addAttribute("target", target);
    model.addAttribute("type", type);
    if(type.equals("신규접수")) {
    	return "claim/claimpage3";
    } else if (type.equals("추가접수")) {
    	return "claim/claimpage3";
    } else {
    	return "에러페이지";
    }
    
	}
	
	@PostMapping("/claimpage4")
	public String claimPage4(
	        @RequestParam("accidentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date accidentDate,
	        @RequestParam("accidentType") String accidentType,
	        @RequestParam(value = "accidentDesc", required = false) String accidentDesc,
	        @RequestParam(value = "diseaseType", required = false) String diseaseType,
	        @RequestParam(value = "diseaseDetail", required = false) String diseaseDetail,
	        @RequestParam("medical_benefits") String medical_benefits,
	        HttpSession session,	
	        Model model) {

		String medicalBenefits = medical_benefits.equals("yes") ? "Y" : "N";
		
	    // ✅ 1. 값 잘 들어오는지 확인
	    System.out.println("사고일: " + accidentDate);
	    System.out.println("사고유형: " + accidentType);
	    System.out.println("사고내용: " + accidentDesc);
	    System.out.println("질병종류: " + diseaseType);
	    System.out.println("진단내용: " + diseaseDetail);
	    System.out.println("의료급여여부: " + medicalBenefits);

	    // ✅ 2. 세션 저장 (필요 시 다음 페이지에서도 활용)
	    session.setAttribute("accidentDate", accidentDate);
	    session.setAttribute("accidentType", accidentType);
	    session.setAttribute("accidentDesc", accidentDesc);
	    session.setAttribute("diseaseType", diseaseType);
	    session.setAttribute("diseaseDetail", diseaseDetail);
	    session.setAttribute("medicalSupport", medicalBenefits);

	    // ✅ 3. 모델로 다음 페이지에 전달
	    model.addAttribute("accidentDate", accidentDate);
	    model.addAttribute("accidentType", accidentType);

	    // ✅ 4. 다음 페이지로 이동
	    return "claim/claimpage4";
	}

	
	@PostMapping("/claimpage5")
	public String uploadClaimFiles(
            @RequestParam("receipt") MultipartFile receiptFile,
            @RequestParam("details") MultipartFile detailFile,
            @RequestParam(value = "etc", required = false) MultipartFile etcFile,
            Model model
    ) {
        try {
            // DB에 청구 데이터 생성 (Claim ID 발급)
            Long claimId = claimService.createNewClaim(); // 간단하게 새 claim 생성
            
            // 파일 저장
            claimService.saveClaimFiles(claimId, receiptFile, detailFile, etcFile);

            model.addAttribute("claimId", claimId);
            return "redirect:/claim/claimpage5"; // 다음 단계 페이지로 이동

        } catch (Exception e) {
            model.addAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "claim/claimpage5";
        }
    }
	
	
	
	// 임시파일 삭제!! 컨트롤러 페이지에서 업로드 후 불안정적으로 나가게 될 시 작동
	@PostMapping("/cancel")
	@ResponseBody
	public void cancelUpload(HttpSession session) {
	    List<String> uploadedFiles = (List<String>) session.getAttribute("uploadedFiles");
	    if (uploadedFiles != null) {
	        for (String fileName : uploadedFiles) {
	            File file = new File("C:/javaweb_yhs/InsuranceWebUploadedFiles/claims/temp/" + fileName);
	            if (file.exists()) file.delete();
	        }
	        session.removeAttribute("uploadedFiles");
	    }
	}
	
	@PostMapping("/claimpage6")
	public String claimPage6(
	        @RequestParam("insuredName") String insuredName,
	        @RequestParam("insuredId1") String insuredId1,
	        @RequestParam("insuredId2") String insuredId2,
	        @RequestParam(value="insuredJob", required=false) String insuredJob,
	        @RequestParam(value="insuredCompany", required=false) String insuredCompany,
	        
	        @RequestParam("beneficiaryName") String beneficiaryName,
	        @RequestParam("beneficiaryId1") String beneficiaryId1,
	        @RequestParam("beneficiaryId2") String beneficiaryId2,
	        @RequestParam("relation") String relation,
	        @RequestParam(value="email1", required=false) String email1,
	        @RequestParam(value="email2", required=false) String email2,
	        @RequestParam(value="postcode", required=false) String postcode,
	        @RequestParam(value="address", required=false) String address,
	        @RequestParam(value="detailAddress", required=false) String detailAddress,
	        
	        @RequestParam(value="bank", required=false) String bank,
	        @RequestParam(value="owner", required=false) String owner,
	        @RequestParam(value="account", required=false) String account,
	        
	        HttpSession session
	) {
	    // 세션에 임시 저장
	    session.setAttribute("insuredName", insuredName);
	    session.setAttribute("insuredId1", insuredId1);
	    session.setAttribute("insuredId2", insuredId2);
	    session.setAttribute("insuredJob", insuredJob);
	    session.setAttribute("insuredCompany", insuredCompany);

	    session.setAttribute("beneficiaryName", beneficiaryName);
	    session.setAttribute("beneficiaryId1", beneficiaryId1);
	    session.setAttribute("beneficiaryId2", beneficiaryId2);
	    session.setAttribute("relation", relation);
	    session.setAttribute("email1", email1);
	    session.setAttribute("email2", email2);
	    session.setAttribute("postcode", postcode);
	    session.setAttribute("address", address);
	    session.setAttribute("detailAddress", detailAddress);

	    session.setAttribute("bank", bank);
	    session.setAttribute("owner", owner);
	    session.setAttribute("account", account);

	    // 다음 동의 페이지로 이동
	    return "claim/claimpage6"; // 뷰 이름
	}
	
	@PostMapping("/finish")
	public String finishClaim(HttpSession session) {
	    // ===== 세션에서 데이터 꺼내오기 =====
	    String beneficiaryName = (String) session.getAttribute("beneficiaryName");
	    String email1 = (String) session.getAttribute("email1");
	    String email2 = (String) session.getAttribute("email2");
	    String bank = (String) session.getAttribute("bank");
	    String account = (String) session.getAttribute("account");
	    String owner = (String) session.getAttribute("owner");
	    String postcode = (String) session.getAttribute("postcode");
	    String address = (String) session.getAttribute("address");
	    String detailAddress = (String) session.getAttribute("detailAddress");

	    String accidentType = (String) session.getAttribute("accidentType");
	    Date accidentDate = (Date) session.getAttribute("accidentDate");
	    String accidentDesc = (String) session.getAttribute("accidentDesc");
	    String medicalBenefits = (String) session.getAttribute("medical_benefits");

	    @SuppressWarnings("unchecked")
	    List<String> uploadedFiles = (List<String>) session.getAttribute("uploadedFiles");

	    // ===== 이메일 조합 =====
	    String email = (email1 != null && email2 != null) ? email1 + "@" + email2 : null;

	    // ===== Claim 객체 생성 =====
	    Claim claim = new Claim();

	    // 필수 기본값
	    claim.setClaim_type("신규");          // 신규 접수
	    claim.setClaim_date(new Date());      // 청구일 = 현재 시각
	    claim.setClaim_status(0);             // 0: 접수(PENDING)
	    claim.setCompletion_date(null);
	    claim.setTotal_paid_amount(0L);

	    // 사고 관련 정보
	    claim.setAccident_type(accidentType);
	    claim.setAccident_date(accidentDate);
	    claim.setAccident_description(accidentDesc);
	    claim.setMedical_benefits(medicalBenefits);

	    // 수익자 정보
	    claim.setBeneficiary_name(beneficiaryName);
	    claim.setBeneficiary_email(email);
	    claim.setBank_name(bank);
	    claim.setBank_account(account);
	    claim.setBeneficiary_postcode(postcode);
	    claim.setBeneficiary_address(
	        (address != null ? address : "") + 
	        (detailAddress != null ? " " + detailAddress : "")
	    );

	    // 예금주(owner)는 은행계좌 소유자명으로 저장
	    if (owner != null && !owner.isEmpty()) {
	        claim.setBeneficiary_name(owner);
	    }

	    // 파일 경로 매핑
	    if (uploadedFiles != null && uploadedFiles.size() >= 2) {
	        claim.setReceipt_file_path(uploadedFiles.get(0));
	        claim.setDetail_file_path(uploadedFiles.get(1));
	        if (uploadedFiles.size() > 2) {
	            claim.setEtc_file_path(uploadedFiles.get(2));
	        }
	    }

	    // 계약/고객 ID (필요 시 세션에서 가져오기)
	    claim.setContract_id((Long) session.getAttribute("contractId"));
	    claim.setCustomer_id((Long) session.getAttribute("customerId"));

	    // ===== Mapper 호출 =====
	    claimMapper.insertClaim(claim);

	    // ===== 세션 정리 =====
	    session.invalidate();

	    // 첫 화면으로 이동
	    return "redirect:/claim/claimpage1";
	}


	private void validateMimeType(MultipartFile file) throws IOException {
	    String contentType = file.getContentType();
	    if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
	        throw new IOException("허용되지 않은 파일 형식입니다: " + contentType);
	    }
	}
	
	// 간단한 JS 이스케이프 (commons-text가 있다면 그걸 써도 좋음)
	private String escapeForJs(String input) {
	    if (input == null) return "";
	    return input
	            .replace("\\", "\\\\")
	            .replace("'", "\\'")
	            .replace("\"", "\\\"")
	            .replace("\r", "")
	            .replace("\n", "\\n");
	}
	
    private String saveFileToTemp(MultipartFile file, String tempDir) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString() + ext;

        File dest = new File(tempDir, savedName);
        file.transferTo(dest);

        // TODO: DB에 메타데이터 저장 (원본명, 저장명, 업로드일 등)
        return savedName;
    }
	
}
