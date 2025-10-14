package com.kd.insuranceweb.common.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDto {
	Integer emp_id;
	String emp_name;
	String username; // 로그인ID
	String password;
	Integer dept_id;
	String is_active;
	Date create_date;
	Date update_date;
	
	List<String> roles;
}
