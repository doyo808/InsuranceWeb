package com.kd.insuranceweb.mall.model.dto;

import lombok.Data;

@Data
public class CoverageDto {
	// coverageId는 INSERT ALL로 DB에서 생성되므로 DTO에 없어도 무방.
	private String cover_name;
    private String cover_desc;
    private double coverage_amount;
    private double base_premium;
    private String cover_required; // "Y"/"N"
    // 추가로 productId를 서비스에서 셋팅하지 않아도 mapper에서 #{productId}로 참조
 
}
