package com.kd.insuranceweb.admin.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.admin.dto.EmployeeDeptDto;
import com.kd.insuranceweb.admin.dto.RoleDto;
import com.kd.insuranceweb.admin.mapper.EmployeeRoleMapper;
import com.kd.insuranceweb.admin.service.RoleService;
import com.kd.insuranceweb.common.dto.AdminUserDetails;
import com.kd.insuranceweb.common.mapper.EmployeeMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/role")
public class RoleController {

	private final RoleService roleService;
	private final EmployeeMapper employeeMapper;
	private final EmployeeRoleMapper employeeRoleMapper;
	
	@GetMapping("/main")
	public String roleManagementPage() {
		return "admin/role/roleMain";
	}

	// ======================
	//   권한편집소
	// ======================
    @GetMapping("/roles")
    public String roleList(Model model) {
	   List<RoleDto> roleList = roleService.getAllRoles();

	    // 서비스 계층에서 null을 반환할 경우를 대비해 빈 리스트로 초기화
	    if (roleList == null) {
	        roleList = new ArrayList<>();
	    }

	    // 데이터가 있든 없든(빈 리스트) 항상 model에 추가
	    model.addAttribute("roles", roleList);
        return "admin/role/_role_list";
    }
    
    // 수정 / 저장 처리 (Inline row)
    @PostMapping("/save")
    public String saveRole(RoleDto role) {
    	System.out.println(role);
    	roleService.update(role);
        return "redirect:/admin/role/roles";
    }

    // 새 권한 추가 처리 (추가 row)
    @PostMapping("/add")
    public String addRole(RoleDto role) {
    	System.out.println(role);
        roleService.insert(role);
        return "redirect:/admin/role/roles";
    }

    // 선택 삭제 처리
    @PostMapping("/deleteAll")
    public String deleteRoles(@RequestParam(name="selected_role_ids", required=false) List<Integer> roleIds) {
    	System.out.println(roleIds);
        if(roleIds != null && !roleIds.isEmpty()) {
            roleService.deleteAll(roleIds);
        }
        return "redirect:/admin/role/roles";
    }

	// ======================
	//   직원편집소
	// ======================
    @GetMapping("/employees")
    public String employeeRoles(@AuthenticationPrincipal AdminUserDetails loginUser, Model model) {

        List<EmployeeDeptDto> employees;

        if (loginUser.getAuthNames().contains("시스템 전체 관리자")) {
            // 전체 사원 조회
            employees = employeeMapper.selectAllWithDept();
        } else if (loginUser.getAuthNames().contains("부서 관리자")) {
            // 본인 부서 사원 조회
            employees = employeeMapper.selectByDeptIdWithDept(loginUser.getDept_id());
        } else {
            employees = null;
        }

        model.addAttribute("employees", employees);
        return "admin/role/_employee_role";
    }


	// ======================
	//   히스토리
	// ======================
    @GetMapping("/history")
    public String history(
            @RequestParam(value = "target_emp_name", required = false) String targetEmpName,
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value = "end_date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
            Model model) {

        var logs = employeeRoleMapper.selectAuthLogs(targetEmpName, startDate, endDate);
        model.addAttribute("logs", logs);

        return "admin/role/_history";
    }

}
