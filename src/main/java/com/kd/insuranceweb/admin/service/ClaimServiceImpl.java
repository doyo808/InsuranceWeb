package com.kd.insuranceweb.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;
import com.kd.insuranceweb.admin.mapper.AdminClaimMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final AdminClaimMapper mapper;

    // 필요하다면 criteria 반영(현재 mapper가 전역 미처리 카운트만 제공하면 파라미터 제거 권장)
    @Override
    public int getPendingCount(ClaimSearchCriteria criteria) {
        return mapper.countPendingClaims();
    }

    @Override
    public int countClaims(ClaimSearchCriteria criteria) {
        return mapper.countClaims(criteria);
    }

    @Override
    public List<ClaimListRowDTO> findClaimsPage(ClaimSearchCriteria criteria) {
        return mapper.findClaimsPage(criteria);
    }

    @Override
    public List<ClaimListRowDTO> findClaimList(ClaimSearchCriteria criteria) {
        // 기존 AdminClaimService.findClaimList 대체
        return mapper.findClaimList(criteria);
    }

    @Override
    public ClaimDetailDTO getClaimDetail(Integer claimId) {
        return mapper.findClaimDetail(claimId);
    }

    @Override
    @Transactional
    public void approveClaim(Integer claimId) {
        mapper.approveClaim(claimId);
    }

    @Override
    @Transactional
    public void rejectClaim(Integer claimId, String reason) {
        mapper.rejectClaim(claimId, reason);
    }
}


