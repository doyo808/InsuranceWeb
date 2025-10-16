package com.kd.insuranceweb.admin.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthLogDto {
    Integer log_id;
    String action_type;
    Integer target_emp_id;
    String target_emp_name;
    Integer role_id;
    String role_name;
    Integer changed_by_id;
    String changed_by_name;
    Date action_date;
    String remarks;
}