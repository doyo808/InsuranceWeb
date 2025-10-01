package com.kd.insuranceweb.mall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MallInsuredDetailDTO {
	String is_smoker;
	String drinks;
	String driving_status;
	String medical_history;
	String medical_history_text;
}
