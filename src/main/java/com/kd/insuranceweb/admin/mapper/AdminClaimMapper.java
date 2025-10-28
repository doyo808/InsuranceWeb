package com.kd.insuranceweb.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;
import com.kd.insuranceweb.admin.dto.CoverageItemDTO;

@Mapper
public interface AdminClaimMapper {
	List<ClaimListRowDTO> findClaimList(@Param("c") ClaimSearchCriteria criteria);

	ClaimDetailDTO findClaimDetail(@Param("claimId") Integer claim_id);

	int approveClaim(@Param("claimId") Integer claim_id,
			@Param("totalPaidAmount") Long totalPaidAmount);

	int rejectClaim(@Param("claimId") Integer claim_id, @Param("reason") String reason);
	
	int countPendingClaims();

	List<ClaimListRowDTO> findClaimsPage(@Param("c") ClaimSearchCriteria criteria);
	
	int countClaims(@Param("c") ClaimSearchCriteria criteria);
	
	List<CoverageItemDTO> findClaimCoverages(Integer claimId);
}
