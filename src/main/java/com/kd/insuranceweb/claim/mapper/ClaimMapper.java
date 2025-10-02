package com.kd.insuranceweb.claim.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.claim.dto.Claim;

import java.util.Date;
import java.util.List;

@Mapper
public interface ClaimMapper {
    void insertClaim(Claim claim);
    Claim selectClaimById(Long claimId);
    List<Claim> selectAllClaims();
    void updateClaim(Claim claim);
    void deleteClaim(Long claimId);
    
    List<Claim> findClaimsByPeriod(
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("offset") int offset,
        @Param("size") int size
    );

    int countClaimsByPeriod(
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );
}
