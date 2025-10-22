package com.kd.insuranceweb.claim.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private final ClaimService claimService;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "text/plain"
    );

    /** 1️⃣ 청구 시작 페이지 */
    @GetMapping("/claimpage1")
    public String claimPage1(HttpSession session) {
        session.removeAttribute("claim");
        session.removeAttribute("personInfo");
        session.removeAttribute("selectedContract");
        return "claim/claimpage1";
    }

    /** 2️⃣ 계약 선택 */
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

    /** 2️⃣ 계약 선택 후 대상 선택 페이지로 이동 */
    @PostMapping("/selectContract")
    public String selectContract(@RequestParam("contractId") Integer contractId, HttpSession session) {

        // Claim 객체 가져오거나 새로 생성
        Claim claim = (Claim) session.getAttribute("claim");
        if (claim == null) claim = new Claim();

        // 계약 ID 저장
        claim.setContract_id(contractId);

        // 세션에 다시 저장
        session.setAttribute("claim", claim);

        // 다음 단계로 이동
        return "claim/claimTargetType";
    }
    
    @GetMapping("/targetType")
    public String claimTargetTypeShow() {
    	return "claim/claimTargetType";
    }
    
    /** 3️⃣ 청구 대상(본인/타인) 선택 */
    @PostMapping("/targetType")
    public String claimTargetType(@RequestParam("target") String target,
                                  @RequestParam("type") String type,
                                  HttpSession session,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails user) {

        session.setAttribute("target", target);
        session.setAttribute("type", type);

        // 세션 Claim 누적
        Claim claim = (Claim) session.getAttribute("claim");
        if (claim == null) claim = new Claim();
        claim.setClaim_type(type);
        session.setAttribute("claim", claim);

        model.addAttribute("target", target);
        model.addAttribute("type", type);

        // 본인일 경우 personInfo 자동 불러오기
        if ("본인".equals(target)) {
            Integer customerId = user.getCustomer_id();
            Map<String, Object> personInfo = claimService.getPersonInfoByCustomerId(customerId);
            
            // ✅ 세션 저장
            session.setAttribute("personInfo", personInfo);
            session.setAttribute("relationDefault", "본인");
            
            // ✅ 모델에도 추가 (뷰에서 즉시 사용 가능)
            model.addAttribute("personInfo", personInfo);
        }

        return "claim/claimPersonInfo";
    }

    /** 4️⃣ 피보험자 및 수익자 정보 입력 */
    @PostMapping("/personInfo")
    public String claimPersonInfo(
            @RequestParam("insuredName") String insuredName,
            @RequestParam("insuredId1") String insuredId1,
            @RequestParam("insuredId2") String insuredId2,
            @RequestParam("beneficiaryName") String beneficiaryName,
            @RequestParam("beneficiaryId1") String beneficiaryId1,
            @RequestParam("beneficiaryId2") String beneficiaryId2,
            @RequestParam(value = "email1", required = false) String email1,
            @RequestParam(value = "email2", required = false) String email2,
            @RequestParam(value = "beneficiaryPostcode", required = false) String postcode,
            @RequestParam(value = "beneficiaryAddress1", required = false) String address,
            @RequestParam(value = "beneficiaryAddress2", required = false) String detailAddress,
            @RequestParam(value = "bank", required = false) String bank,
            @RequestParam(value = "owner", required = false) String owner,
            @RequestParam(value = "account", required = false) String account,
            HttpSession session
    ) {
        Claim claim = (Claim) session.getAttribute("claim");
        if (claim == null) claim = new Claim();

        claim.setBeneficiary_name(beneficiaryName);
        claim.setBank_name(bank);
        claim.setBank_account(account);
        claim.setBeneficiary_email((email1 != null ? email1 : "") + (email2 != null ? "@" + email2 : ""));
        claim.setBeneficiary_postcode(postcode);
        claim.setBeneficiary_address(address + (detailAddress != null ? " " + detailAddress : ""));

        session.setAttribute("claim", claim);
        return "claim/claimAccidentInfo";
    }

    /** 5️⃣ 사고 정보 입력 */
    @PostMapping("/accidentInfo")
    public String claimAccidentInfo(
            @RequestParam("accidentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date accidentDate,
            @RequestParam("accidentType") String accidentType,
            @RequestParam(value = "accidentDesc", required = false) String accidentDesc,
            @RequestParam("medical_benefits") String medical_benefits,
            HttpSession session,
            Model model) {

        String medicalBenefits = medical_benefits.equals("yes") ? "Y" : "N";

        Claim claim = (Claim) session.getAttribute("claim");
        if (claim == null) claim = new Claim();

        claim.setAccident_date(accidentDate);
        claim.setAccident_type(accidentType);
        claim.setAccident_description(accidentDesc);
        claim.setMedical_benefits(medicalBenefits);

        session.setAttribute("claim", claim);
        model.addAttribute("accidentDate", accidentDate);
        model.addAttribute("accidentType", accidentType);

        return "claim/claimDocument";
    }

    /** 6️⃣ 서류 업로드 */
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

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String folderName = "customer_" + customerId + "_" + timestamp;
            String uploadDir = System.getProperty("user.dir") + "/uploaded/" + folderName + "/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            Claim claim = (Claim) session.getAttribute("claim");
            if (claim == null) claim = new Claim();

            if (!receipt.isEmpty()) {
                String fileName = "receipt_" + System.currentTimeMillis() + "_" + receipt.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(receipt.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                claim.setReceipt_file_path("uploaded/" + folderName + "/" + fileName);
            }

            if (!details.isEmpty()) {
                String fileName = "details_" + System.currentTimeMillis() + "_" + details.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(details.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                claim.setDetail_file_path("uploaded/" + folderName + "/" + fileName);
            }

            if (etc != null && !etc.isEmpty()) {
                String fileName = "etc_" + System.currentTimeMillis() + "_" + etc.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(etc.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                claim.setEtc_file_path("uploaded/" + folderName + "/" + fileName);
            }

            session.setAttribute("claim", claim);
            return "redirect:/claim/claimpage6"; // 동의서 페이지로 이동

        } catch (Exception e) {
            log.error("❌ 파일 업로드 실패", e);
            model.addAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "claim/claimDocument";
        }
    }

    /** 7️⃣ 동의서 체크 */
    @GetMapping("/claimpage6")
    public String claimPage6() {
        return "claim/claimpage6";
    }

    /** 8️⃣ 최종 완료 처리 */
    @PostMapping("/finish")
    public String finishClaim(HttpSession session,
                              @AuthenticationPrincipal CustomUserDetails user,
                              Model model) {
        try {
            Claim claim = (Claim) session.getAttribute("claim");
            if (claim == null) throw new IllegalStateException("세션에 청구 정보가 없습니다.");

            // ✅ 청구일 자동 설정 (현재 시간)
            claim.setClaim_date(new Date());
            claim.setClaim_status(1);
            
            // ✅ DB에 insert → claim_id 생성
            claimService.insertClaim(claim);
            Integer claimId = claim.getClaim_id();

            // ✅ 기존 폴더 경로 추출
            String oldFolderName = claim.getDetail_file_path().split("/")[1]; // uploaded/다음 폴더명
            String oldFolderPath = System.getProperty("user.dir") + "/uploaded/" + oldFolderName;
            File oldDir = new File(oldFolderPath);

            // ✅ 새 폴더 생성 (/uploaded/{claimId})
            String newFolderPath = System.getProperty("user.dir") + "/uploaded/" + claimId;
            File newDir = new File(newFolderPath);
            if (!newDir.exists()) newDir.mkdirs();

            // ✅ 파일 이동
            if (oldDir.exists()) {
                for (File file : oldDir.listFiles()) {
                    Files.move(file.toPath(),
                            Paths.get(newDir.getAbsolutePath(), file.getName()),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                oldDir.delete();
            }

            // ✅ Claim 경로 업데이트
            if (claim.getDetail_file_path() != null)
                claim.setDetail_file_path("uploaded/" + claimId + "/" +
                        Paths.get(claim.getDetail_file_path()).getFileName().toString());
            if (claim.getReceipt_file_path() != null)
                claim.setReceipt_file_path("uploaded/" + claimId + "/" +
                        Paths.get(claim.getReceipt_file_path()).getFileName().toString());
            if (claim.getEtc_file_path() != null)
                claim.setEtc_file_path("uploaded/" + claimId + "/" +
                        Paths.get(claim.getEtc_file_path()).getFileName().toString());

            // ✅ DB update로 경로 반영
            claimService.updateClaimFilePaths(claim);
            
            session.removeAttribute("claim");
            session.removeAttribute("personInfo");
            session.removeAttribute("selectedContract");

            return "redirect:/claim/claimFinish";

        } catch (Exception e) {
            log.error("❌ 보험금 청구 등록 실패", e);
            model.addAttribute("errorMessage", "보험금 청구 등록 중 오류가 발생했습니다.");
            return "claim/claimpage6";
        }
    }

    /** 완료 페이지 */
    @GetMapping("/claimFinish")
    public String claimFinishPage() {
        return "claim/claimFinish";
    }
}
