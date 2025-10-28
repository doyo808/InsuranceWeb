package com.kd.insuranceweb.mypage.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDto {
	Integer payment_id;
	Integer contract_id;
	Integer paid_amount;
	Date payment_date;
	String pay_status;
	
	String product_name;
	Integer total_premium;
}
