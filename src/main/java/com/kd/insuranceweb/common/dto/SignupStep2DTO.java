package com.kd.insuranceweb.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupStep2DTO {
	String login_id;
	String password;
	boolean marketingAgree;
}
