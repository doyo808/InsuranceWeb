package com.kd.insuranceweb.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.test.dto.Employee;
import com.kd.insuranceweb.test.mapper.EmployeeMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/emp")
@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@GetMapping("/list")
	public void list(Model model) {
		List<Employee> employees = employeeMapper.selectAll();
		
		for (Employee emp : employees) {
			log.info("{}", emp);
		}
		
		model.addAttribute("employees", employees);
	}
}
