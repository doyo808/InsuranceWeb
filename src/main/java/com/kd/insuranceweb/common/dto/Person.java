package com.kd.insuranceweb.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Person {
	Integer person_id;
	String person_name;
	String email;
	String personal_id;
	String phone_number;
}
