package com.kd.insuranceweb.claim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/claim")
public class ClaimController {
	
    final private String tempDir = "C:/javaweb_yhs/InsuranceWebUploadedFiles/claims/temp/";
    final private String finalDir = "/upload/claims/";
    
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
	//        @RequestParam("accidentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date accidentDate,
	        @RequestParam("accidentType") String accidentType,
	        @RequestParam(value = "accidentDesc", required = false) String accidentDesc,
	        @RequestParam(value = "diseaseType", required = false) String diseaseType,
	        @RequestParam(value = "diseaseDetail", required = false) String diseaseDetail,
	        @RequestParam("medicalSupport") String medicalSupport,
	        HttpSession session,
	        Model model
	) {
	    // ✅ 세션 만료시간 30분(1800초) 설정
	    session.setMaxInactiveInterval(30 * 60);

	    // ✅ 세션에 값 저장
//	    session.setAttribute("accidentDate", accidentDate);
	    session.setAttribute("accidentType", accidentType);
	    session.setAttribute("accidentDesc", accidentDesc);
	    session.setAttribute("diseaseType", diseaseType);
	    session.setAttribute("diseaseDetail", diseaseDetail);
	    session.setAttribute("medicalSupport", medicalSupport);

	    // 뷰에 전달
	  //  model.addAttribute("accidentDate", accidentDate);
	    model.addAttribute("accidentType", accidentType);
	    model.addAttribute("accidentDesc", accidentDesc);
	    model.addAttribute("diseaseType", diseaseType);
	    model.addAttribute("diseaseDetail", diseaseDetail);
	    model.addAttribute("medicalSupport", medicalSupport);

	    // ✅ 다음 파일 업로드 페이지로 이동
	    return "claim/claimpage4"; 
	}
	
	@PostMapping("/claimpage5")
	public String uploadFiles(
	        @RequestParam("receipt") MultipartFile receipt,
	        @RequestParam("details") MultipartFile details,
	        @RequestParam(value = "etc", required = false) MultipartFile etc,
	        Model model,
	        HttpSession session
	) {
	    long size_max = 3 * 1024 * 1024; // byte단위
	    
	    try {
	        if (receipt.isEmpty() || details.isEmpty() || etc == null || etc.isEmpty()) {
	            throw new IOException("필수 파일이 누락되었습니다.");
	        }
	        
	        if ((receipt != null && receipt.getSize() > size_max) ||
	        	    (details != null && details.getSize() > size_max) ||
	        	    (etc != null && etc.getSize() > size_max)) {

	        	    throw new IOException("각 파일의 크기가 3MB를 초과했습니다.");
	        }
	        // ✅ MIME 타입 검사
	        validateMimeType(receipt);
	        validateMimeType(details);
	        validateMimeType(etc);

	        // 세션에 파일 정보 저장
	        List<String> uploadedFiles = (List<String>) session.getAttribute("uploadedFiles");
	        if (uploadedFiles == null) uploadedFiles = new ArrayList<>();

	        uploadedFiles.add(saveFileToTemp(receipt, tempDir));
	        uploadedFiles.add(saveFileToTemp(details, tempDir));
	        uploadedFiles.add(saveFileToTemp(etc, tempDir));

	        session.setAttribute("uploadedFiles", uploadedFiles);

	        model.addAttribute("successMessage", "업로드 성공!");
	        return "claim/claimpage5"; 
	    } catch (IOException e) {
	        model.addAttribute("errorMessage", escapeForJs(e.getMessage()));
	        return "claim/claimpage4"; 
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
	    String insuredName = (String) session.getAttribute("insuredName");
	    String insuredId1 = (String) session.getAttribute("insuredId1");
	    String insuredId2 = (String) session.getAttribute("insuredId2");
	    String beneficiaryName = (String) session.getAttribute("beneficiaryName");
	    String beneficiaryId1 = (String) session.getAttribute("beneficiaryId1");
	    String beneficiaryId2 = (String) session.getAttribute("beneficiaryId2");
	    String email1 = (String) session.getAttribute("email1");
	    String email2 = (String) session.getAttribute("email2");
	    String bank = (String) session.getAttribute("bank");
	    String account = (String) session.getAttribute("account");
	    String owner = (String) session.getAttribute("owner");

	    @SuppressWarnings("unchecked")
	    List<String> uploadedFiles = (List<String>) session.getAttribute("uploadedFiles");

	    // ===== 가공 (앞뒤 합치기) =====
	    String insuredId = insuredId1 + insuredId2;
	    String beneficiaryId = beneficiaryId1 + beneficiaryId2;
	    String email = (email1 != null && email2 != null) ? email1 + "@" + email2 : null;

	    // ===== Claim 객체 생성 =====
	    Claim claim = new Claim();
	    
//	    claim.setCustomerId(3001L); // TODO: 실제 고객 ID 가져오기
//	    claim.setClaimType((String) session.getAttribute("type")); // 신규접수/추가접수 등
//	    claim.setClaimDate(new Date());
//	    claim.setCompensationType("H"); // TODO: 세션/화면에서 받아온 값 쓰기
//	    claim.setAccidentDate((Date) session.getAttribute("accidentDate"));
//	    claim.setAccidentDescription((String) session.getAttribute("accidentDescription"));
//
//	    claim.setInsuredName(insuredName);
//	    claim.setInsuredId(insuredId);
//	    claim.setBeneficiaryName(beneficiaryName);
//	    claim.setBeneficiaryId(beneficiaryId);
//	    claim.setRelation((String) session.getAttribute("relation"));
//	    claim.setEmail(email);
//	    claim.setPostcode((String) session.getAttribute("postcode"));
//	    claim.setAddress((String) session.getAttribute("address"));
//	    claim.setDetailAddress((String) session.getAttribute("detailAddress"));
//
//	    claim.setBankName(bank);
//	    claim.setBankAccount(account);
//	    claim.setOwner(owner);
//
//	    // 파일 경로 (업로드된 파일들 매핑)
//	    if (uploadedFiles != null && uploadedFiles.size() >= 2) {
//	        claim.setReceiptFilePath(uploadedFiles.get(0));
//	        claim.setDetailFilePath(uploadedFiles.get(1));
//	        if (uploadedFiles.size() > 2) {
//	            claim.setEtcFilePath(uploadedFiles.get(2));
//	        }
//	    }
//
//	    claim.setClaimStatus(1); // 진행중 상태
//
//	    // ===== Mapper 호출 =====
//	    claimMapper.insertClaim(claim);

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
