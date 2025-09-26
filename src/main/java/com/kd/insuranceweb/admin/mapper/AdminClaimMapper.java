package com.kd.insuranceweb.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.admin.dto.ClaimDetailDTO;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;

@Mapper
public interface AdminClaimMapper {
	 List<ClaimListRowDTO> findClaimList();

	    ClaimDetailDTO findClaimDetail(@Param("claim_id") Integer claim_id);

	    int approveClaim(@Param("claim_id") Integer claim_id);

	    int rejectClaim(@Param("claim_id") Integer claim_id, @Param("reason") String reason);
}
