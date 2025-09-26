package com.kd.insuranceweb.mall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MallPersonalBasicDTO {
	String insured_name;
	String insured_phone_number;
	String insured_email;
	String customer_name;
}
