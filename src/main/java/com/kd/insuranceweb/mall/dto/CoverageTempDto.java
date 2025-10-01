package com.kd.insuranceweb.mall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoverageTempDto {
	private int id;
	private String name;
	private int premium;
	private int amount;
}
