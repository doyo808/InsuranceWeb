package com.kd.insuranceweb.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.common.dto.EmployeeDto;

@Mapper
public interface EmployeeMapper {
	EmployeeDto findByLoginId(@Param("username") String username);

	List<String> findRolesByEmpId(@Param("emp_id") Integer emp_id);
}
