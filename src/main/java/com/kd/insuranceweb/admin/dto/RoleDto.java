package com.kd.insuranceweb.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto {
	Integer role_id;
	String role_name;
	String role_desc;
}
