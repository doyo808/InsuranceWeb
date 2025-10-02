package com.kd.insuranceweb.claim.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kd.insuranceweb.claim.service.ClaimService;
import com.kd.insuranceweb.claim.service.FileService;
import com.kd.insuranceweb.common.dto.CustomUserDetails;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private FileService fileService;

    // 페이지 이동 (단순 뷰 반환 → Controller 책임)
    @GetMapping("/claimpage1")
    public String claimPage1() {
        return "claim/claimpage1";
    }

    @GetMapping("/claimpage2")
    public String claimPage2() {
        return "claim/claimpage2";
    }

    @PostMapping("/claimpage3")
    public String claimPage3(@RequestParam("target") String target,
                             @RequestParam("type") String type,
                             HttpSession session,
                             Model model) {
        session.setAttribute("target", target);
        session.setAttribute("type", type);
        session.setMaxInactiveInterval(30 * 60); // 세션 30분 유지

        model.addAttribute("target", target);
        model.addAttribute("type", type);

        if ("신규접수".equals(type) || "추가접수".equals(type)) {
            return "claim/claimpage3";
        } else {
            return "에러페이지";
        }
    }

    @PostMapping("/claimpage4")
    public String claimPage4(@RequestParam("accidentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date accidentDate,
                             @RequestParam("accidentType") String accidentType,
                             @RequestParam(value = "accidentDesc", required = false) String accidentDesc,
                             @RequestParam(value = "diseaseType", required = false) String diseaseType,
                             @RequestParam(value = "diseaseDetail", required = false) String diseaseDetail,
                             @RequestParam("medicalSupport") String medicalSupport,
                             HttpSession session,
                             Model model) {
        session.setMaxInactiveInterval(30 * 60);

        claimService.saveAccidentInfo(session, accidentDate, accidentType, accidentDesc, diseaseType, diseaseDetail, medicalSupport);

        model.addAttribute("accidentDate", accidentDate);
        model.addAttribute("accidentType", accidentType);
        model.addAttribute("accidentDesc", accidentDesc);
        model.addAttribute("diseaseType", diseaseType);
        model.addAttribute("diseaseDetail", diseaseDetail);
        model.addAttribute("medicalSupport", medicalSupport);

        return "claim/claimpage4";
    }

    @PostMapping("/claimpage5")
    public String uploadFiles(@RequestParam("receipt") MultipartFile receipt,
                              @RequestParam("details") MultipartFile details,
                              @RequestParam(value = "etc", required = false) MultipartFile etc,
                              Model model,
                              HttpSession session

                              ) {
        try {
            List<String> uploadedFiles = fileService.handleFileUpload(receipt, details, etc);
            session.setAttribute("uploadedFiles", uploadedFiles);
            model.addAttribute("successMessage", "업로드 성공!");
            return "claim/claimpage5";
        } catch (IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "claim/claimpage4";
        }
    }

    @PostMapping("/cancel")
    @ResponseBody
    public void cancelUpload(HttpSession session) {
        List<String> uploadedFiles = (List<String>) session.getAttribute("uploadedFiles");
        fileService.cancelUpload(uploadedFiles);
        session.removeAttribute("uploadedFiles");
    }

    @PostMapping("/claimpage6")
    public String claimPage6(
                             @RequestParam("beneficiaryName") String beneficiaryName,
                             @RequestParam("beneficiaryId1") String beneficiaryId1,
                             @RequestParam("beneficiaryId2") String beneficiaryId2,
                             @RequestParam("relation") String relation,
                             @RequestParam(value="email1", required=false) String email1,
                             @RequestParam(value="email2", required=false) String email2,
                             @RequestParam(value="beneficiaryPostcode", required=false) String postcode,
                             @RequestParam(value="beneficiaryAddress", required=false) String address,
                             @RequestParam(value="detailAddress", required=false) String detailAddress,
                             @RequestParam(value="bank", required=false) String bank,
                             @RequestParam(value="owner", required=false) String owner,
                             @RequestParam(value="account", required=false) String account,
                             HttpSession session) {

        claimService.saveBeneficiaryInfo(session,
                                         beneficiaryName, beneficiaryId1, beneficiaryId2,
                                         relation, email1, email2,
                                         postcode, address, detailAddress,
                                         bank, owner, account);

        return "claim/claimpage6";
    }

    @PostMapping("/finish")
    public String finishClaim(HttpSession session,
    		  @AuthenticationPrincipal CustomUserDetails loginUser,
    		  Model model) {
        claimService.finishClaim(session, loginUser, model);
        try {
			fileService.moveFilesToFinal(session);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "redirect:/claim/claimpage1";
    }
    
    
    @GetMapping("/inquirypage1")
    public String inquiry( ) {
    	return "/claim/inquirypage1";
    }
    
    @GetMapping("/claims/search")
    @ResponseBody
    public Map<String, Object> searchClaims(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return claimService.findClaimsByPeriod(startDate, endDate, page, size);
    }
    
}