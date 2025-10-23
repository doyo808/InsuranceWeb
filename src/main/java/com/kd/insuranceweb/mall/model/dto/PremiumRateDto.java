package com.kd.insuranceweb.mall.model.dto;

import lombok.Data;

@Data
public class PremiumRateDto {
	// rateRowId는 INSERT ALL로 DB에서 생성되므로 DTO에 없어도 무방.
	private long product_id;
	private int age_from;
    private int age_to;
    private String gender;
    private int is_smoker;
    private double age_weight;
    private double gender_weight;
    
    public void setIs_smoker(boolean is_smoker) {
    	this.is_smoker = is_smoker ? 1 : 0;
    }
}
