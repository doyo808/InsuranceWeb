package com.kd.insuranceweb.claim;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.claim.dto.Claim;

@Mapper
public interface ClaimMapper {
    
    List<Claim> selectAllClaims();
    
    void updateClaim(Claim claim);
    
    void deleteClaim(Long claimId);

    void insertClaim(Claim claim);

    Claim selectClaimById(Long claimId);

    void updateClaimFilePaths(
        @Param("claimId") Long claimId,
        @Param("detailFilePath") String detail,
        @Param("receiptFilePath") String receipt,
        @Param("etcFilePath") String etc
    );

}
