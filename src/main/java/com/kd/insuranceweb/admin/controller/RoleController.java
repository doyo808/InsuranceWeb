package com.kd.insuranceweb.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.admin.dto.RoleDto;
import com.kd.insuranceweb.admin.service.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/role")
public class RoleController {

	private final RoleService roleService;
	
	@GetMapping("/main")
	public String roleManagementPage() {
		return "admin/role/roleMain";
	}

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

    @GetMapping("/employees")
    public String employeeRoles(Model model) {
//        model.addAttribute("employees", empService.getAll());
        return "admin/role/_employee_role";
    }

    @GetMapping("/history")
    public String history(Model model) {
//        model.addAttribute("logs", logService.getAll());
        return "admin/role/_history";
    }
}
