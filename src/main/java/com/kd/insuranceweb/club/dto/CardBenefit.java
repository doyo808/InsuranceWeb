package com.kd.insuranceweb.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CardBenefit {
	private Integer benefitId;
	private Integer cardCompanyId;
	private String applicableProducts;
	private String interestFreeMonths;
	private String partialInterestMonths;
	private String pointUsageRule;
	private Date startDate;
	private Date endDate;
}
