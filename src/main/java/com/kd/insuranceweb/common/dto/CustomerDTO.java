package com.kd.insuranceweb.common.dto;

import java.sql.Date;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
	// ================
	//     DB항목
	// ================
	Integer customer_id;
	Integer person_id;
	String address_1;
	String address_2;
	String job;
	String company_name;
	String job_address1;
	String job_address2;
	String marketing_agree_yn;
	String login_id;
	String password_hash;
	Date created_at;
	Date deleted_at;
	
	// 전화번호 정규식 상수
	private static final String PHONE_PATTERN = "^(01[016789]-\\d{3,4}-\\d{4})|(0\\d{1,2}-\\d{3,4}-\\d{4})$";
	@Pattern(regexp = PHONE_PATTERN, message = "전화번호 형식이 올바르지 않습니다. (예: 010-0000-0000, 02-123-4567)")
	String home_number;
	@Pattern(regexp = PHONE_PATTERN, message = "전화번호 형식이 올바르지 않습니다. (예: 010-0000-0000, 02-123-4567)")
	String job_phone_number;
	
	// 우편번호 정규식 상수
	private static final String ZIP_PATTERN = "\\d{5}";
	@Pattern(regexp = ZIP_PATTERN, message = "우편번호 형식이 올바르지 않습니다. (예: 12345)")
	String zip_code;
	@Pattern(regexp = ZIP_PATTERN, message = "우편번호 형식이 올바르지 않습니다. (예: 12345)")
	String job_zip_code;
	
	
	// ================
	//  	조회용
	// ================
	String person_name;
	String phone_number;
	String email;
}
