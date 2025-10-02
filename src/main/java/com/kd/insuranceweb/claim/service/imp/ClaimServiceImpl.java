package com.kd.insuranceweb.claim.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.kd.insuranceweb.claim.dto.Claim;
import com.kd.insuranceweb.claim.mapper.ClaimMapper;
import com.kd.insuranceweb.claim.service.ClaimService;
import com.kd.insuranceweb.common.dto.CustomUserDetails;
import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.common.dto.PersonDTO;
import com.kd.insuranceweb.common.mapper.CustomerMapper;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class ClaimServiceImpl implements ClaimService {
	private final Integer defaultPaidAmount = 10000;
	
    @Autowired
    private ClaimMapper claimMapper;
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private PersonMapper personMapper;

    @Override
    public void saveAccidentInfo(HttpSession session,
                                 Date accidentDate, String accidentType,
                                 String accidentDesc, String diseaseType,
                                 String diseaseDetail, String medicalSupport) {

        session.setAttribute("accidentDate", accidentDate);
        session.setAttribute("accidentType", accidentType);
        session.setAttribute("accidentDesc", accidentDesc);
        session.setAttribute("diseaseType", diseaseType);
        session.setAttribute("diseaseDetail", diseaseDetail);
        session.setAttribute("medicalSupport", medicalSupport);
    }

    @Override
    public void saveBeneficiaryInfo(HttpSession session,
                                    String beneficiaryName, String beneficiaryId1, String beneficiaryId2,
                                    String relation, String email1, String email2,
                                    String postcode, String address, String detailAddress,
                                    String bank, String owner, String account) {
        session.setAttribute("beneficiaryName", beneficiaryName);
        
        session.setAttribute("beneficiaryId1", beneficiaryId1);
        session.setAttribute("beneficiaryId2", beneficiaryId2);
        if (!beneficiaryId1.matches("\\d{6}") || !beneficiaryId2.matches("\\d{7}")) {
            throw new IllegalArgumentException("잘못된 주민등록번호 형식입니다.");
        }
        session.setAttribute("relation", relation);
        session.setAttribute("email1", email1);
        session.setAttribute("email2", email2);
        session.setAttribute("postcode", postcode);
        session.setAttribute("address", address);
        session.setAttribute("detailAddress", detailAddress);

        session.setAttribute("bank", bank);
        session.setAttribute("owner", owner);
        session.setAttribute("account", account);
    }

    @Override
    public void finishClaim(HttpSession session,
    		@AuthenticationPrincipal CustomUserDetails loginUser,
            Model model) {

        String beneficiaryId1 = (String) session.getAttribute("beneficiaryId1");
        String beneficiaryId2 = (String) session.getAttribute("beneficiaryId2");
        String email1 = (String) session.getAttribute("email1");
        String email2 = (String) session.getAttribute("email2");

        @SuppressWarnings("unchecked")
        List<String> uploadedFiles = (List<String>) session.getAttribute("uploadedFiles");
        
        // 가공
        String beneficiaryId = beneficiaryId1 + beneficiaryId2;
        String email = (email1 != null && email2 != null) ? email1 + "@" + email2 : null;
        
        PersonDTO person = null;
        CustomerDTO customer = null;
        if (loginUser != null) {
            person = personMapper.selectById(loginUser.getPerson_id());
            customer = customerMapper.selectByLoginId(loginUser.getUsername());

            if (person != null && customer != null) {
                loginUser.setPerson(person);
                loginUser.setCustomer(customer);

                model.addAttribute("loginUser", loginUser);
            }
        }
        
        // Claim 생성
        Claim claim = new Claim();
        claim.setCustomerId(customer.getCustomer_id());
        claim.setClaimType((String) session.getAttribute("type"));
        claim.setClaimDate(new Date());
        claim.setCompensationType("H");
        claim.setAccidentDate((Date) session.getAttribute("accidentDate"));
        claim.setAccidentDescription((String) session.getAttribute("accidentDesc"));
        claim.setBeneficiaryName((String) session.getAttribute("beneficiaryName"));
        claim.setBeneficiaryEmail(email);
        claim.setTotalPaidAmount(defaultPaidAmount);
        claim.setBeneficiaryPostcode((String) session.getAttribute("postcode"));
        claim.setBeneficiaryAddress((String) session.getAttribute("address") + (String) session.getAttribute("detailAddress"));
        claim.setBankName((String) session.getAttribute("bank"));
        claim.setBankAccount((String) session.getAttribute("account"));
        if (uploadedFiles != null && uploadedFiles.size() >= 2) {
            claim.setReceiptFilePath(uploadedFiles.get(0));
            claim.setDetailFilePath(uploadedFiles.get(1));
            if (uploadedFiles.size() > 2) {
                claim.setEtcFilePath(uploadedFiles.get(2));
            }
        }
        claim.setClaimStatus(1);
        claimMapper.insertClaim(claim);
    }
    
    public Map<String, Object> findClaimsByPeriod(Date startDate, Date endDate, int page, int size) {
        int offset = page * size;
        List<Claim> claims = claimMapper.findClaimsByPeriod(startDate, endDate, offset, size);
        int total = claimMapper.countClaimsByPeriod(startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        result.put("content", claims);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (int) Math.ceil((double) total / size));
        return result;
    }
    
}
