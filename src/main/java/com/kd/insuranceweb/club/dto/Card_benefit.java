package com.kd.insuranceweb.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Card_benefit {
	private Integer benefit_id;
	private Integer card_company_id;
	private String applicable_products;
	private String interest_free_months;
	private String partial_interest_months;
	private String point_usage_rule;
	private Date start_date;
	private Date end_date;
}
