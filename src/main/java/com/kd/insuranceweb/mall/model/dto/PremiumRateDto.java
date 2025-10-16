package com.kd.insuranceweb.mall.model.dto;

import lombok.Data;

@Data
public class PremiumRateDto {
	// rateRowId는 INSERT ALL로 DB에서 생성되므로 DTO에 없어도 무방.
	private int age_from;
    private int age_to;
    private String gender;
    private boolean is_smoker;
    private double age_weight;
    private double gender_weight;
}
