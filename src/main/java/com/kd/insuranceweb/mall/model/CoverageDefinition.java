package com.kd.insuranceweb.mall.model;

import lombok.Data;

@Data
public class CoverageDefinition {
	/*
	 
	 coverage_id	NUMBER	담보 정의 ID	PK
    product_id	NUMBER	보험상품 ID	FK → insurance_products
    coverage_category	NUMBER	카테고리 ID	FK -> coverage_categories
    product_plan	NUMBER(1)	플랜단계	1,2,3
    cover_name	VARCHAR(100)	담보 이름	NOT NULL
    cover_desc	VARCHAR(2000)	설명	(선택)
    coverage_amount	NUMBER(15,2)	보장 보험금	(선택)
    base_premium	NUMBER(15,2)	기본 보험료	(선택)
    cover_required	VARCHAR(2)	필수 담보	Y/N
	 
	 */
	
	Integer coverage_def_id;
	Integer product_id;
	Integer coverage_category;
	Integer product_plan;
	String cover_name;
	String cover_desc;
	Integer coverage_amount;
	Integer base_premium;
	String cover_required;
}
