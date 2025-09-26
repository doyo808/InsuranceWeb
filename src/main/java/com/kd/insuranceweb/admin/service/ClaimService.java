package com.kd.insuranceweb.admin.service;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;

public interface ClaimService {
	  ClaimDetailDTO getClaimDetail(Integer claimId);
	  void approve(Integer claimId);
	  void reject(Integer claimId, String reason);
}
