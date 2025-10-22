package com.kd.insuranceweb.mall.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductRequestDTO {
	
	private Long productId;        // insertInsuranceProduct의 selectKey가 채움
	private String productType;
    private String product_name;
    private String product_desc;
    private String thumbnail;
    private String conditions;
	
	private List<CoverageDto> coverages;
    private List<PremiumRateDto> premiumRates;

}
