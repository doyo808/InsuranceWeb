package com.kd.insuranceweb.claim.service;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;

import com.kd.insuranceweb.common.dto.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

public interface ClaimService {

    void saveAccidentInfo(HttpSession session,
                          Date accidentDate, String accidentType,
                          String accidentDesc, String diseaseType,
                          String diseaseDetail, String medicalSupport);

    void saveBeneficiaryInfo(HttpSession session,
                             String beneficiaryName, String beneficiaryId1, String beneficiaryId2,
                             String relation, String email1, String email2,
                             String postcode, String address, String detailAddress,
                             String bank, String owner, String account);

    void finishClaim(HttpSession session,
    		@AuthenticationPrincipal CustomUserDetails loginUser,
            Model model);
    
    Map<String, Object> findClaimsByPeriod(
    		Date startDate, Date endDate, 
    		int page, int size);
    
    
}
