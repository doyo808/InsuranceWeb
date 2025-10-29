package com.kd.insuranceweb.mall.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PremiumRateVO {
	private final int rate_row_id;
	private final int product_id;
	private final int age_from;
    private final int age_to;
    private final String gender;
    private final int is_smoker;
    private final double age_weight;
    private final double gender_weight;
}
