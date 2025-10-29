package com.kd.insuranceweb.mall.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoverageVO {
	private final int coverage_id;
	private final int product_id;
	private final String cover_name;
    private final String cover_desc;
    private final double coverage_amount;
    private final double base_premium;
    private final String cover_required; // "Y"/"N"
 
}
