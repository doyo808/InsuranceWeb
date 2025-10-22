package com.kd.insuranceweb.admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminActivityLogDTO {
    private Integer emp_id;
    private String emp_name;
    private String action_type;
    private String action_desc;
    private LocalDateTime created_at;
}
