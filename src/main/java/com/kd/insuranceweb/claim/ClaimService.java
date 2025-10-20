package com.kd.insuranceweb.claim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kd.insuranceweb.claim.dto.Claim;

import jakarta.servlet.http.HttpSession;

@Service
public class ClaimService {

	@Autowired
    private ClaimMapper claimMapper;

    // 신규 청구 데이터 생성 (간단한 claim_id 발급)
    public Long createNewClaim() {
        Claim claim = new Claim();
        claimMapper.insertClaim(claim);
        return claim.getClaim_id();
    }
    
 // 최종 insert
    public Long saveFinalClaim(HttpSession session) {
        Claim claim = new Claim();

        // ✅ 세션에 저장된 값 꺼내오기
        claim.setAccident_date((Date) session.getAttribute("accidentDate"));
        claim.setAccident_description((String) session.getAttribute("accidentDesc"));
        claim.setAccident_type((String) session.getAttribute("accidentType")); // 사고유형
        claim.setMedical_benefits((String) session.getAttribute("medicalBenefits")); // 의료급여 수급권자 여부

        // ✅ 파일 경로
        claim.setReceipt_file_path((String) session.getAttribute("receiptPath"));
        claim.setDetail_file_path((String) session.getAttribute("detailPath"));
        claim.setEtc_file_path((String) session.getAttribute("etcPath"));

        // ✅ 수익자 정보
        claim.setBeneficiary_name((String) session.getAttribute("beneficiaryName"));
        claim.setBank_account((String) session.getAttribute("bankAccount"));
        claim.setBank_name((String) session.getAttribute("bankName"));
        claim.setBeneficiary_email((String) session.getAttribute("beneficiaryEmail"));
        claim.setBeneficiary_postcode((String) session.getAttribute("beneficiaryPostcode"));
        claim.setBeneficiary_address((String) session.getAttribute("beneficiaryAddress"));

        // ✅ 기본값
        claim.setClaim_type("신규접수");           // VARCHAR
        claim.setClaim_status(1);             // Integer (예: 0=PENDING, 1=APPROVED 등)
        claim.setClaim_date(new Date());      // DATE
        claim.setCompletion_date(null);       // 완료일은 없음
        claim.setTotal_paid_amount(0L);       // 기본 0원

        // ✅ 계약/고객 정보 (필요 시 세션에서 추가)
        claim.setContract_id((Long) session.getAttribute("contractId"));
        claim.setCustomer_id((Long) session.getAttribute("customerId"));

        // ✅ DB INSERT 실행
        claimMapper.insertClaim(claim);

        // ✅ claim_id 반환
        return claim.getClaim_id();
    }


    // 파일 저장 로직
    public void saveClaimFiles(Long claimId, MultipartFile receipt, MultipartFile detail, MultipartFile etc) throws IOException {
        String baseDir = System.getProperty("user.dir") + "/uploaded/claims/" + claimId + "/";
        Files.createDirectories(Paths.get(baseDir));

        String receiptPath = saveFile(baseDir, receipt);
        String detailPath = saveFile(baseDir, detail);
        String etcPath = etc != null && !etc.isEmpty() ? saveFile(baseDir, etc) : null;

        claimMapper.updateClaimFilePaths(claimId, detailPath, receiptPath, etcPath);
    }

    private String saveFile(String baseDir, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(baseDir + uniqueName);
        file.transferTo(filePath);
        return filePath.toString();
    }

    public String getFilePath(Long claimId, String type) {
        Claim claim = claimMapper.selectClaimById(claimId);
        return switch (type) {
            case "receipt" -> claim.getReceipt_file_path();
            case "detail" -> claim.getDetail_file_path();
            case "etc" -> claim.getEtc_file_path();
            default -> throw new IllegalArgumentException("Invalid file type: " + type);
        };
    }
}
