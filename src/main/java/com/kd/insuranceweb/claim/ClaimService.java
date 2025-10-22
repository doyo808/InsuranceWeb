package com.kd.insuranceweb.claim;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.claim.dto.Claim;
import com.kd.insuranceweb.claim.dto.ContractDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimMapper claimMapper;

    /** 보험금 청구 DB 저장 */
    public void insertClaim(Claim claim) {
        try {
            log.info("📝 ClaimService.insertClaim 실행 - {}", claim);
            claimMapper.insertClaim(claim);
            log.info("✅ 보험금 청구 insert 성공 (claim_id={})", claim.getClaim_id());
        } catch (Exception e) {
            log.error("❌ ClaimService.insertClaim 실패", e);
            throw new RuntimeException("보험금 청구 저장 실패: " + e.getMessage(), e);
        }
    }
    
    public void updateClaimFilePaths(Claim claim) {
        claimMapper.updateClaimFilePaths(claim);
    }

    /** 고객의 계약 목록 조회 (contractsChoice) */
    public List<ContractDTO> getContractsByCustomer(Integer customerId, Integer months) {
        return claimMapper.getContractsByCustomer(customerId, months);
    }
    
    public Map<String, Object> getPersonInfoByCustomerId(Integer customerId) {
        return claimMapper.selectPersonInfoByCustomerId(customerId);
    }
}
