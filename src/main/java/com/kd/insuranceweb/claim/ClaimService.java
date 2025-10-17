package com.kd.insuranceweb.claim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClaimService {

    @Autowired
    private ClaimMapper claimMapper;

    // 신규 청구 데이터 생성 (간단한 claim_id 발급)
    public Long createNewClaim() {
        Claim claim = new Claim();
        claimMapper.insertClaim(claim);
        return claim.getClaimId();
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
            case "receipt" -> claim.getReceiptFilePath();
            case "detail" -> claim.getDetailFilePath();
            case "etc" -> claim.getEtcFilePath();
            default -> throw new IllegalArgumentException("Invalid file type: " + type);
        };
    }
}
