package com.kd.insuranceweb.common.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
	// DB항목
	Integer customer_id;
	Integer person_id;
	String zip_code;
	String address_1;
	String address_2;
	String home_number;
	String job;
	String company_name;
	String job_zip_code;
	String job_address1;
	String job_address2;
	String job_phone_number;
	String marketing_agree_yn;
	String login_id;
	String password_hash;
	Date created_at;
	Date deleted_at;
	
	// 조회용 
	String person_name;
	String phone_number;
	String email;
}
