package com.kd.insuranceweb.admin.service;

import java.util.List;

import com.kd.insuranceweb.admin.dto.AdminActivityLogDTO;

public interface AdminActivityService {
    void recordActivity(Long employeeId, String employeeName, String actionType, String actionDesc, String targetId);
    List<AdminActivityLogDTO> getRecentActivities();
}
