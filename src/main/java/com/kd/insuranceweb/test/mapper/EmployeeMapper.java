package com.kd.insuranceweb.test.mapper;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kd.insuranceweb.test.dto.Employee;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class EmployeeMapper {
	
	private final SqlSessionTemplate sql;
	
	public List<Employee> selectAll() {
		return sql.selectList("Employees.selectAll");
	}
	public Employee selectOne(Integer employee_id) {
		return sql.selectOne("Employees.selectOne", employee_id);
	}
}