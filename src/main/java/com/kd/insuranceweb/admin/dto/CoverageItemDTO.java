package com.kd.insuranceweb.admin.dto;

import lombok.Data;

@Data
public class CoverageItemDTO {
	private String cover_name;
    private String cover_desc;
    private Long coverage_amount;
}
