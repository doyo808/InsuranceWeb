package com.kd.insuranceweb.admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminActivityLogDTO {
	private Long log_id;
    private Long employee_id;
    private String employee_name;
    private String action_type;
    private String action_desc;
    private String target_id;
    private LocalDateTime created_at;
}
