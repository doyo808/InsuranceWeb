package com.kd.insuranceweb.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.dto.AdminActivityLogDTO;
import com.kd.insuranceweb.admin.mapper.AdminActivityMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminActivityServiceImpl implements AdminActivityService {
	
	private final AdminActivityMapper mapper;

	@Override
	public void recordActivity(Integer employeeId, String employeeName, String actionType, String actionDesc) {
		mapper.insertActivity(
				new AdminActivityLogDTO(employeeId, employeeName, actionType, actionDesc, null));
	}

	@Override
	public List<AdminActivityLogDTO> getRecentActivities() {
		return mapper.getRecentActivities();
	}
}
