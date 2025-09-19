package com.kd.insuranceweb.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Card_benefit_interest_free {
	private Integer benefit_id;
	private String phone_number;
	private Integer card_company_id;
	private String card_company_name;
	private String img_src;
	private String interest_free_months;
	private String partial_interest_months;
	private String months_details;
	private Date start_date;
	private Date end_date;
}
