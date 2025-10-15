package com.kd.insuranceweb.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.dto.RoleDto;
import com.kd.insuranceweb.admin.mapper.RoleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
	
	private final RoleMapper mapper;
	
	public List<RoleDto> getAllRoles() {
		return mapper.selectAll();
	}
	
}
