package com.kd.insuranceweb.mall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InsuredDTO {
	Integer insured_id;
	Integer person_id;
	String gender;
	Integer age;
	String is_smoker;
	String drinks;
	String driving_status;
	String medical_history;
}
