package com.kd.insuranceweb.admin.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.admin.mapper.EmployeeRoleMapper;
import com.kd.insuranceweb.admin.mapper.RoleMapper;
import com.kd.insuranceweb.common.dto.AdminUserDetails;
import com.kd.insuranceweb.common.mapper.EmployeeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/role/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeRoleController {

    private final EmployeeMapper employeeMapper;
    private final RoleMapper roleMapper;
    private final EmployeeRoleMapper employeeRoleMapper;

    @GetMapping("/{emp_id}/roles")
    public String showEmployeeRoles(@PathVariable("emp_id") Integer emp_id, Model model) {
        var employee = employeeMapper.findById(emp_id);
        var roles = employeeRoleMapper.findRolesByEmployee(emp_id);
        var allRoles = roleMapper.selectAll();

        model.addAttribute("employee", employee);
        model.addAttribute("roles", roles);
        model.addAttribute("allRoles", allRoles);
        return "admin/role/_employee_role_detail";
    }

    @PostMapping("/{emp_id}/grant")
    public String grantRole(@PathVariable("emp_id") Integer emp_id,
                            @RequestParam("role_id") Integer role_id,
                            @AuthenticationPrincipal AdminUserDetails loginUser) {

        // 권한 추가
        employeeRoleMapper.grantRoleToEmployee(emp_id, role_id);

        // 로그 기록
        employeeRoleMapper.insertAuthLog(
                "GRANT",
                emp_id,
                role_id,
                loginUser.getEmp_id(),
                "사원 권한 부여"
        );

        log.info("[권한부여] 실행자={}, 대상={}, 권한={}", loginUser.getUsername(), emp_id, role_id);
        return "redirect:/admin/role/employee/" + emp_id + "/roles";
    }

    @PostMapping("/{emp_id}/revoke")
    public String revokeRole(@PathVariable("emp_id") Integer emp_id,
                             @RequestParam("role_id") Integer role_id,
                             @AuthenticationPrincipal AdminUserDetails loginUser) {

        // 권한 삭제
        employeeRoleMapper.revokeRoleFromEmployee(emp_id, role_id);

        // 로그 기록
        employeeRoleMapper.insertAuthLog(
                "REVOKE",
                emp_id,
                role_id,
                loginUser.getEmp_id(),
                "사원 권한 회수"
        );

        log.info("[권한회수] 실행자={}, 대상={}, 권한={}", loginUser.getUsername(), emp_id, role_id);
        return "redirect:/admin/role/employee/" + emp_id + "/roles";
    }
}
