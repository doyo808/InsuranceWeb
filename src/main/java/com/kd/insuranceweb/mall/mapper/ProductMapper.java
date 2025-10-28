package com.kd.insuranceweb.mall.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.mall.model.dto.CoverageDto;
import com.kd.insuranceweb.mall.model.dto.PremiumRateDto;
import com.kd.insuranceweb.mall.model.dto.ProductDTO;
import com.kd.insuranceweb.mall.model.dto.ProductRequestDTO;

@Mapper
public interface ProductMapper {

	 // 1. 상품 등록 (selectKey로 productId 채움)
    int insertInsuranceProduct(ProductRequestDTO product);

    // 2. 담보들 일괄 INSERT ALL
    int insertCoverageDefinition(CoverageDto product);
    
    // 3. 요율들 일괄 INSERT ALL
    int insertPremiumRateRow(PremiumRateDto product);
    
    // 상품 가입시 필요한 데이터 조회(담보와 간단한 상품정보)
    ProductDTO selectProductWithCoverages(Long id);
}
