package com.kd.insuranceweb.admin.service;

import java.util.List;

import com.kd.insuranceweb.admin.dto.AdminActivityLogDTO;

public interface AdminActivityService {
    void recordActivity(Integer employee_id, String employee_name, String action_type, String action_desc);
    
    List<AdminActivityLogDTO> getRecentActivities();
}
