package com.kd.insuranceweb.claim;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ClaimMapper {
    void insertClaim(Claim claim);
    Claim selectClaimById(Long claimId);
    List<Claim> selectAllClaims();
    void updateClaim(Claim claim);
    void deleteClaim(Long claimId);
}
