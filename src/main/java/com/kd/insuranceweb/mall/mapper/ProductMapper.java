package com.kd.insuranceweb.mall.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.mall.model.dto.ProductRequestDTO;

@Mapper
public interface ProductMapper {

	 // 1. 상품 등록 (selectKey로 productId 채움)
    int insertInsuranceProduct(ProductRequestDTO product);

    // 2. 담보들 일괄 INSERT ALL
    int insertCoverageDefinitions(ProductRequestDTO product); // product.productId & product.coverages 사용

    // 3. 요율들 일괄 INSERT ALL
    int insertPremiumRateRows(ProductRequestDTO product); // product.productId & product.premiumRates 사용

}
