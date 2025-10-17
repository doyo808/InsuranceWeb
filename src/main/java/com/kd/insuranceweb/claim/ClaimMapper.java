package com.kd.insuranceweb.claim;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClaimMapper {
    void insertClaim(Claim claim);
    
    Claim selectClaimById(Long claimId);
    
    List<Claim> selectAllClaims();
    
    void updateClaim(Claim claim);
    
    void deleteClaim(Long claimId);
    
    void updateClaimFilePaths(@Param("claimId") Long claimId,
            @Param("detailFilePath") String detail,
            @Param("receiptFilePath") String receipt,
            @Param("etcFilePath") String etc);

}
