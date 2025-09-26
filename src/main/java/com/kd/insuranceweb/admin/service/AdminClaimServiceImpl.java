package com.kd.insuranceweb.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.mapper.AdminClaimMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminClaimServiceImpl implements ClaimService {

  private final AdminClaimMapper adminClaimMapper;  // ← DAO 주입

  @Override
  @Transactional(readOnly = true)
  public ClaimDetailDTO getClaimDetail(Integer claimId) {
    return adminClaimMapper.findClaimDetail(claimId);
  }

  @Override
  @Transactional
  public void approve(Integer claimId) {
    int updated = adminClaimMapper.approveClaim(claimId);
    if (updated != 1) {
      throw new IllegalStateException("승인 업데이트 실패: claimId=" + claimId);
    }
  }

  @Override
  @Transactional
  public void reject(Integer claimId, String reason) {
    int updated = adminClaimMapper.rejectClaim(claimId, reason);
    if (updated != 1) {
      throw new IllegalStateException("거절 업데이트 실패: claimId=" + claimId);
    }
  }
}
