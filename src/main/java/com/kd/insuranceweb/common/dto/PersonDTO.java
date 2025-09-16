package com.kd.insuranceweb.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDTO {
	private Integer person_id;
	private String person_name;
	private String email;
	private String personal_id;
	private String phone_number;
}
