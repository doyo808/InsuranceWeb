package com.kd.insuranceweb.admin.service;

import java.util.List;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;

public interface ClaimService {
	int countClaims(ClaimSearchCriteria criteria); // ★ 추가

	List<ClaimListRowDTO> findClaimsPage(ClaimSearchCriteria criteria); // ★ 추가

	ClaimDetailDTO getClaimDetail(Integer claimId);

	void approve(Integer claimId);

	void reject(Integer claimId, String reason);

	int getPendingCount();
}
