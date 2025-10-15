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

	public int update(RoleDto role) {
		return mapper.update(role);
	}

	public int insert(RoleDto role) {
		return mapper.insert(role);
	}

	public int deleteAll(List<Integer> roleIds) {
		return mapper.deleteAll(roleIds);
	}
	
}
