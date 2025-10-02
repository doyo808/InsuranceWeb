package com.kd.insuranceweb.mall.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PremiumRateRow {
	/*
		rate_row_id	NUMBER	PK	PK
	    rate_table_id	NUMBER	요율표 ID	FK → premium_rate_tables
	    age_from	NUMBER(3)	나이 시작	NOT NULL
	    age_to	NUMBER(3)	나이 끝	NOT NULL
	    gender	VARCHAR(2)	성별	M,F,A
	    age_weight	NUMBER(1,2)	나이 가중치	DEFAULT 0.5
	    gender_weight	NUMBER(1,2)	성별 가중치	DEFAULT 0.10
	    is_smoker	BOOLEAN	흡연 여부	DEFAULT FALSE
	*/
	
	Integer rate_row_id;
//	Integer rate_table_id;
	Integer product_id;
	Integer age_from;
	Integer age_to;
	String gender;
	Double age_weight;
	Double gender_weight;
	Integer is_smoker;
}
