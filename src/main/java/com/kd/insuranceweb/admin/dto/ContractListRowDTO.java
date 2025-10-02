package com.kd.insuranceweb.admin.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ContractListRowDTO {
	private Integer contract_id;
	
	private LocalDate start_date;
	private LocalDate end_date;
	
	private Long total_premium;
	
	private String status;
	
	// persons table
	private String person_name;	
	
	// insurance_products table
	private String product_name;
	private String product_types;
	
	
}
