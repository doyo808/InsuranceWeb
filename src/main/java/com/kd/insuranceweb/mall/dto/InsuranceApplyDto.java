package com.kd.insuranceweb.mall.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InsuranceApplyDto {
	private int productId;
	private String productName;
	private List<CoverageTempDto> selectedCoverages;
	private int totalPremium;
}
