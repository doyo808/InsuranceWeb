package com.kd.insuranceweb.mall.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.mall.model.dto.CoverageDto;
import com.kd.insuranceweb.mall.model.dto.PremiumRateDto;
import com.kd.insuranceweb.mall.model.dto.ProductRequestDTO;

@Mapper
public interface ProductMapper {

	 // 1. 상품 등록 (selectKey로 productId 채움)
    int insertInsuranceProduct(ProductRequestDTO product);

    // 2. 담보들 일괄 INSERT ALL
    int insertCoverageDefinition(CoverageDto product);
    
    // 3. 요율들 일괄 INSERT ALL
    int insertPremiumRateRow(PremiumRateDto product);
}
