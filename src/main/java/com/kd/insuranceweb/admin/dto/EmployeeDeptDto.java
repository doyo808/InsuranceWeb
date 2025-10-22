package com.kd.insuranceweb.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDeptDto {
    private Integer emp_id;
    private String emp_name;
    private String dept_name;
}