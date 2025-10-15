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
	public void recordActivity(Long employeeId, String employeeName, String actionType, String actionDesc,
			String targetId) {
		mapper.insertActivity(
				new AdminActivityLogDTO(null, employeeId, employeeName, actionType, actionDesc, targetId, null));
	}

	@Override
	public List<AdminActivityLogDTO> getRecentActivities() {
		return mapper.getRecentActivities();
	}
}
