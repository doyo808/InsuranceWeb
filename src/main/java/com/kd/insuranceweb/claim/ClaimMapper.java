package com.kd.insuranceweb.claim;

import java.time.LocalDate;
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

    Claim selectClaimById(Integer claimId);

    void updateClaimFilePaths(Claim claim);

    Map<String, Object> selectPersonInfoByCustomerId(@Param("customer_id") Integer customerId);
    
    List<Claim> selectClaimsByCustomer(@Param("customer_id") int customerId,
            @Param("months") Integer months,
            @Param("start") String start,
            @Param("end") String end);
    
    /** ✅ 개월 단위 조회 (3개월, 6개월, 1년 등) */
    List<Claim> selectClaimsByMonths(
            @Param("customerId") Integer customerId,
            @Param("months") Integer months
        );

    /** ✅ 날짜 직접입력 조회 (start~end 범위) */
    List<Claim> selectClaimsByDates(
            @Param("customerId") Integer customerId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
        );


}
