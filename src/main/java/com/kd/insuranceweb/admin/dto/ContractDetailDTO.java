package com.kd.insuranceweb.admin.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ContractDetailDTO {
	
	// 고객 정보
	// customers table -> persons table
	  private String customer_name; // 고객명 
	  private String phone_number;
	  private String email;
	  
	// 피보험자 정보
	// insureds table
	  private String insured_name; // 피보험자명
	  private String gender;
	  private LocalDate birth_date;
	  private String is_smoker; // 흡연여부
	  private String drinks; // 음주여부
	  private String driving_status; // 운전상태
	  private String medical_history; // 병력
	
	/*
		계약자와 피보험자 불일치 시 동의서 확인
	*/  
	  
	// 상품정보
	// insurance_products table
	  private String product_name;
	  private String product_types;
	  
	// insurance_contracts table
	  private Integer contract_id; // 계약ID
	  private LocalDate start_date; // 계약 시작일
	  private LocalDate end_date;
	  
	  private Long total_premium; // 한달 납입 보험료
	  
	  private String status; // 계약 상태
	
}
