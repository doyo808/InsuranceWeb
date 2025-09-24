package com.kd.insuranceweb.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Card_benefit_view {
	private Integer benefit_id;
    private Integer company_id;
    private String company_name;
    private String phone_number;

    private String interest_free_months;
    private String partial_interest_months;
    private String point_usage_rule;
    private String applicable_products;
    private Date start_date;
    private Date end_date;
}
