package com.kd.insuranceweb.admin.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductListRowDTO {
	private Integer product_id;
	private String product_types;
	private String product_name;
	private LocalDate start_date;
	private LocalDate end_date;
	private LocalDate updated_at;
	
}
