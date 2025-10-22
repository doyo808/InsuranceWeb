package com.kd.insuranceweb.admin.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto {
	Integer role_id;
	String role_name;
	String role_desc;
	Date grant_date; // 부여일자 표시용
}
