package com.kd.insuranceweb.claim.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kd.insuranceweb.claim.ClaimService;
import com.kd.insuranceweb.claim.dto.Claim;
import com.kd.insuranceweb.claim.dto.ContractDTO;
import com.kd.insuranceweb.common.dto.CustomUserDetails;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimController {
	
	@Value("${file.upload-dir}")
	private String baseUploadDir;

    private final ClaimService claimService;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "text/plain"
    );
    
 // 청구내역 확인
    @GetMapping("/claimList")
    public String getClaimList(
            @RequestParam(name = "months", required = false, defaultValue = "3") Integer months,
            @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal CustomUserDetails user,
            Model model) {

        // 로그인한 사용자
        Integer customerId = user.getCustomer_id();

     // --- 기간 계산 (months가 null이 아닌 경우)
        if (months != null && months > 0) {
            endDate = LocalDate.now();
            startDate = endDate.minusMonths(months);
        }
        
        // 기간에 맞게 데이터 조회
        List<Claim> txns = claimService.getClaimsByDateRange(customerId, months, startDate, endDate);

        model.addAttribute("txns", txns);
        model.addAttribute("months", months);
        model.addAttribute("start", startDate);
        model.addAttribute("end", endDate);

        return "claim/chkClaims";
    }



    // ---------------------------
    // 1️⃣ 청구 시작
    // ---------------------------
    @GetMapping("/claimpage1")
    public String claimPage1(HttpSession session) {
        session.removeAttribute("claim");
        session.removeAttribute("personInfo");
        session.removeAttribute("selectedContract");
        session.removeAttribute("beneficiaryInfo");
        session.removeAttribute("target");
        session.removeAttribute("type");
        session.removeAttribute("relationDefault");
        return "claim/claimpage1";
    }

    // ---------------------------
    // 2️⃣ 계약 선택
    // ---------------------------
    @GetMapping("/contractsChoice")
    public String getMyContracts(@AuthenticationPrincipal CustomUserDetails user,
                                 @RequestParam(value = "months", required = false) Integer months,
                                 Model model) {

        Integer customerId = user.getCustomer_id();
        List<ContractDTO> contracts = claimService.getContractsByCustomer(customerId, months);

        model.addAttribute("contracts", contracts);
        model.addAttribute("months", months);
        return "claim/claim_contract_list";
    }

    @PostMapping("/selectContract")
    public String selectContract(@RequestParam("contractId") Integer contractId, HttpSession session) {
        Claim claim = getOrCreateClaim(session);
        claim.setContract_id(contractId);
        session.setAttribute("claim", claim);

        return "claim/claimTargetType";
    }

    // ---------------------------
    // 3️⃣ 청구 대상/유형 선택
    // ---------------------------
    @GetMapping("/targetType")
    public String claimTargetTypeShow(HttpSession session, Model model) {
    	addClaimToModel(session, model);
        return "claim/claimTargetType";
    }

    @PostMapping("/targetType")
    public String claimTargetType(@RequestParam("target") String target,
                                  @RequestParam("type") String type,
                                  @AuthenticationPrincipal CustomUserDetails user,
                                  HttpSession session,
                                  Model model) {

        session.setAttribute("target", target);
        session.setAttribute("type", type);

        Claim claim = getOrCreateClaim(session);
        claim.setClaim_type(type);
        session.setAttribute("claim", claim);

        // 본인일 경우 개인정보 자동 채움
        if ("본인".equals(target)) {
            Integer customerId = user.getCustomer_id();
            Map<String, Object> personInfo = claimService.getPersonInfoByCustomerId(customerId);
            session.setAttribute("personInfo", personInfo);
            session.setAttribute("relationDefault", "본인");
            model.addAttribute("personInfo", personInfo);
        }

        model.addAttribute("target", target);
        model.addAttribute("type", type);
        return "claim/claimPersonInfo";
    }

    // ---------------------------
    // 4️⃣ 피보험자 / 수익자 정보
    // ---------------------------
    @GetMapping("/personInfo")
    public String claimPersonInfoShow(HttpSession session, Model model) {
    	addClaimToModel(session, model);
        return "claim/claimPersonInfo";
    }

    @PostMapping("/personInfo")
    public String claimPersonInfo(
            @RequestParam("insuredName") String insuredName,
            @RequestParam("insuredId1") String insuredId1,
            @RequestParam("insuredId2") String insuredId2,
            @RequestParam("beneficiaryName") String beneficiaryName,
            @RequestParam(value = "relation", required = false) String relation,
            @RequestParam(value = "email1", required = false) String email1,
            @RequestParam(value = "email2", required = false) String email2,
            @RequestParam(value = "beneficiaryPostcode", required = false) String postcode,
            @RequestParam(value = "beneficiaryAddress1", required = false) String address,
            @RequestParam(value = "beneficiaryAddress2", required = false) String detailAddress,
            @RequestParam(value = "bank", required = false) String bank,
            @RequestParam(value = "owner", required = false) String owner,
            @RequestParam(value = "account", required = false) String account,
            HttpSession session) {

        // ✅ Claim 객체 업데이트
        Claim claim = getOrCreateClaim(session);
        claim.setBeneficiary_name(beneficiaryName);
        claim.setBank_name(bank);
        claim.setBank_account(account);
        claim.setBeneficiary_email((email1 != null ? email1 : "") + (email2 != null ? "@" + email2 : ""));
        claim.setBeneficiary_postcode(postcode);
        claim.setBeneficiary_address(address + (detailAddress != null ? " " + detailAddress : ""));
        session.setAttribute("claim", claim);

        // ✅ 수익자 정보 Map으로 세션에 별도 저장
        Map<String, Object> beneficiaryInfo = new HashMap<>();
        beneficiaryInfo.put("name", beneficiaryName);
        beneficiaryInfo.put("insuredId1", insuredId1);
        beneficiaryInfo.put("insuredId2", insuredId2);
        beneficiaryInfo.put("relation", relation);
        beneficiaryInfo.put("email1", email1);
        beneficiaryInfo.put("email2", email2);
        beneficiaryInfo.put("postcode", postcode);
        beneficiaryInfo.put("address1", address);
        beneficiaryInfo.put("address2", detailAddress);
        beneficiaryInfo.put("bank", bank);
        beneficiaryInfo.put("owner", owner);
        beneficiaryInfo.put("account", account);

        session.setAttribute("beneficiaryInfo", beneficiaryInfo);

        return "claim/claimAccidentInfo";
    }


    // ---------------------------
    // 5️⃣ 사고 정보
    // ---------------------------
    @GetMapping("/accidentInfo")
    public String claimAccidentInfoShow(HttpSession session, Model model) {
    	addClaimToModel(session, model);
        return "claim/claimAccidentInfo";
    }

    @PostMapping("/accidentInfo")
    public String claimAccidentInfo(
            @RequestParam("accidentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date accidentDate,
            @RequestParam("accidentType") String accidentType,
            @RequestParam(value = "accidentDesc", required = false) String accidentDesc,
            @RequestParam("medical_benefits") String medical_benefits,
            @RequestParam(value = "diseaseType", required = false) String diseaseType,   // ✅ name 일치
            HttpSession session,
            Model model) {

        // ✅ 세션에서 claim 객체 꺼내거나 새로 생성
        Claim claim = getOrCreateClaim(session);

        // ✅ 값 매핑
        claim.setAccident_date(accidentDate);
        claim.setAccident_type(accidentType);
        claim.setAccident_description(accidentDesc);
        claim.setDisease_type(diseaseType);    // ✅ name="diseaseType"과 일치
        claim.setMedical_benefits("yes".equals(medical_benefits) ? "Y" : "N");

        // ✅ 세션에 갱신된 claim 저장
        session.setAttribute("claim", claim);

        // ✅ 다음 페이지로 데이터 전달 (필요 시)
        model.addAttribute("claim", claim);

        return "claim/claimDocument"; // 다음 페이지 (서류등록 화면)
    }

    // ---------------------------
    // 6️⃣ 서류 업로드
    // ---------------------------
    @GetMapping("/uploadDocuments")
    public String claimDocumentShow(HttpSession session, Model model) {
    	addClaimToModel(session, model);
        return "claim/claimDocument";
    }

    @PostMapping("/uploadDocuments")
    public String uploadDocuments(
            @RequestParam("receipt") MultipartFile receipt,
            @RequestParam("details") MultipartFile details,
            @RequestParam(value = "etc", required = false) MultipartFile etc,
            @AuthenticationPrincipal CustomUserDetails user,
            HttpSession session,
            Model model) {

        try {
            Integer customerId = (user != null && user.getCustomer_id() != null)
                    ? user.getCustomer_id() : 0;

            // 업로드 폴더 생성
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String folderName = "customer_" + customerId + "_" + timestamp;
            String uploadDir = baseUploadDir + folderName + "/";
            new File(uploadDir).mkdirs();

            Claim claim = getOrCreateClaim(session);

            // 파일 업로드 및 경로 저장
            claim.setReceipt_file_path(saveFile(receipt, uploadDir, "receipt", folderName));
            claim.setDetail_file_path(saveFile(details, uploadDir, "details", folderName));
            claim.setEtc_file_path(saveFile(etc, uploadDir, "etc", folderName));

            session.setAttribute("claim", claim);
            return "redirect:/claim/claimpage6";

        } catch (Exception e) {
            log.error("❌ 파일 업로드 실패", e);
            model.addAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "claim/claimDocument";
        }
    }

    // ---------------------------
    // 7️⃣ 동의서
    // ---------------------------
    @GetMapping("/claimpage6")
    public String claimPage6() {
        return "claim/claimpage6";
    }

    // ---------------------------
    // 8️⃣ 최종 완료
    // ---------------------------
    @PostMapping("/finish")
    public String finishClaim(HttpSession session,
                              @AuthenticationPrincipal CustomUserDetails user,
                              Model model) {
        try {
            Claim claim = (Claim) session.getAttribute("claim");
            if (claim == null) throw new IllegalStateException("세션에 청구 정보가 없습니다.");

            // DB insert
            claim.setClaim_date(new Date());
            claim.setClaim_status(1);
            claimService.insertClaim(claim);
            Integer claimId = claim.getClaim_id();

            // 업로드 폴더 이동
            moveUploadedFiles(claim, claimId);

            // DB에 최종 파일 경로 반영
            claimService.updateClaimFilePaths(claim);

            // 세션 초기화
            session.removeAttribute("claim");
            session.removeAttribute("personInfo");
            session.removeAttribute("selectedContract");
            session.removeAttribute("beneficiaryInfo");
            session.removeAttribute("target");
            session.removeAttribute("type");
            session.removeAttribute("relationDefault");

            return "redirect:/claim/claimFinish";

        } catch (Exception e) {
            log.error("❌ 보험금 청구 등록 실패", e);
            model.addAttribute("errorMessage", "보험금 청구 등록 중 오류가 발생했습니다.");
            return "claim/claimpage6";
        }
    }

    @GetMapping("/claimFinish")
    public String claimFinishPage() {
        return "claim/claimFinish";
    }

    // ==========================================================
    // 🔧 내부 유틸리티 메서드
    // ==========================================================
    private Claim getOrCreateClaim(HttpSession session) {
        Claim claim = (Claim) session.getAttribute("claim");
        if (claim == null) claim = new Claim();
        return claim;
    }

    private String saveFile(MultipartFile file, String uploadDir, String prefix, String folderName) throws Exception {
        if (file == null || file.isEmpty()) return null;
        String fileName = prefix + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "uploaded/" + folderName + "/" + fileName;
    }

    private void moveUploadedFiles(Claim claim, Integer claimId) throws Exception {
        String oldFolderName = claim.getDetail_file_path().split("/")[1];
        File oldDir = new File(System.getProperty("user.dir") + "/uploaded/" + oldFolderName);
        File newDir = new File(System.getProperty("user.dir") + "/uploaded/" + claimId);

        if (!newDir.exists()) newDir.mkdirs();

        if (oldDir.exists()) {
            for (File file : oldDir.listFiles()) {
                Files.move(file.toPath(), Paths.get(newDir.getAbsolutePath(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
            oldDir.delete();
        }

        if (claim.getDetail_file_path() != null)
            claim.setDetail_file_path("uploaded/" + claimId + "/" + Paths.get(claim.getDetail_file_path()).getFileName().toString());
        if (claim.getReceipt_file_path() != null)
            claim.setReceipt_file_path("uploaded/" + claimId + "/" + Paths.get(claim.getReceipt_file_path()).getFileName().toString());
        if (claim.getEtc_file_path() != null)
            claim.setEtc_file_path("uploaded/" + claimId + "/" + Paths.get(claim.getEtc_file_path()).getFileName().toString());
    }
    
    private void addClaimToModel(HttpSession session, Model model) {
        Claim claim = (Claim) session.getAttribute("claim");
        Map<String, Object> personInfo = (Map<String, Object>) session.getAttribute("personInfo");
        Map<String, Object> beneficiaryInfo = (Map<String, Object>) session.getAttribute("beneficiaryInfo");

        if (claim != null) model.addAttribute("claim", claim);
        if (personInfo != null) model.addAttribute("personInfo", personInfo);
        if (beneficiaryInfo != null) model.addAttribute("beneficiaryInfo", beneficiaryInfo);
    }
}
