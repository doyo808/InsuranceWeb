package com.kd.insuranceweb.mall.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CoverageMapper {
	 /**
     * 계약 담보 항목 추가
     * @param contractId 계약 ID
     * @param coverageId 담보 정의 ID
     * @param coverageAmount 보장 금액
     * @return 삽입된 행 수 (성공 시 1)
     */
    int insertCoverage(
            @Param("contract_id") Integer contractId,
            @Param("coverage_id") Integer coverageId,
            @Param("coverage_amount") BigDecimal coverageAmount
    );
}
