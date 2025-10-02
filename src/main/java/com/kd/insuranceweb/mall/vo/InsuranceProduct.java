package com.kd.insuranceweb.mall.vo;

import java.util.Date;

import lombok.Data;

@Data
public class InsuranceProduct {
	/*
	 
	 product_id	NUMBER	상품 ID (PK)	PK
    product_types	VARCHAR(20)	보험유형	
    product_name	VARCHAR(255)	상품 이름	NOT NULL
    product_desc	VARCHAR(2000)	상품 설명	(선택)
    start_date	DATE	판매 시작일	(선택)
    end_date	DATE	판매 종료일	(선택)
    thumbnail_url	VARCHAR(255)	이미지 URL	(선택)
    conditions_path	VARCHAR(200)	약관 파일	NOT NULL
    created_at	DATETIME	생성일	DEFAULT CURRENT
    updated_at	DATETIME	수정일	ON UPDATE
	 
	 */
	
	Integer product_id;
	String product_types;
	String product_name;
	String product_desc;
	Date start_date;
	Date end_date;
	String thumbnail_url;
	String conditions_path;
	Date create_at;
	Date update_at;
}
