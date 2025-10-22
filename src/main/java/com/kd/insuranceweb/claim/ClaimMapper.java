package com.kd.insuranceweb.claim;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.claim.dto.Claim;
import com.kd.insuranceweb.claim.dto.ContractDTO;

@Mapper
public interface ClaimMapper {
    
    List<Claim> selectAllClaims();
    
    List<ContractDTO> getContractsByCustomer(
            @Param("customer_id") Integer customerId,
            @Param("months") Integer months
        );
    
    void updateClaim(Claim claim);
    
    void deleteClaim(Long claimId);

    void insertClaim(Claim claim);

    Claim selectClaimById(Long claimId);

    void updateClaimFilePaths(Claim claim);

    Map<String, Object> selectPersonInfoByCustomerId(@Param("customer_id") Integer customerId);
}
