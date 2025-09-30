package com.kd.insuranceweb.admin.service;

import java.util.List;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;


public interface ClaimService {

    // 미처리 건수 (criteria 반영할지 여부를 결정)
    int getPendingCount(ClaimSearchCriteria criteria);

    // 전체 목록 카운트(검색조건 포함)
    int countClaims(ClaimSearchCriteria criteria);

    // 페이지 목록 조회
    List<ClaimListRowDTO> findClaimsPage(ClaimSearchCriteria criteria);

    // (선택) 페이징 없이 조건 목록 조회가 필요하면 명시적으로 분리
    List<ClaimListRowDTO> findClaimList(ClaimSearchCriteria criteria);

    // 상세
    ClaimDetailDTO getClaimDetail(Integer claimId);

    // 승인/거절
    void approveClaim(Integer claimId);
    void rejectClaim(Integer claimId, String reason);
}

