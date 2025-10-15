package com.kd.insuranceweb.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.admin.dto.RoleDto;

@Mapper
public interface RoleMapper {
	List<RoleDto> selectAll();
}
