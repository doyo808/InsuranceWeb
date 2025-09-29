package com.kd.insuranceweb.admin.dto;

import lombok.Data;

//예: 계약 전용
@Data
public class ContractSearchCriteria extends CommonSearchCriteria {
	private String productCode; // 상품코드
	private String channel; // 가입채널
}