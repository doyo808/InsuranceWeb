package com.kd.insuranceweb.common.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminUserDetails implements UserDetails, LoginUser {

	private Integer emp_id;
	private String emp_name;
	private Integer dept_id;
	private String username;
	private String password;
	private List<GrantedAuthority> authorities;
	private List<String> authNames;
	
	public boolean hasRole(String roleName) {
	    return authNames != null && authNames.contains(roleName);
	}
	
	public AdminUserDetails(Integer emp_id, String emp_name, Integer dept_id, String username, String password,
			List<GrantedAuthority> authorities, List<String> authNames) {
		super();
		this.emp_id = emp_id;
		this.emp_name = emp_name;
		this.dept_id = dept_id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.authNames = authNames;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public Integer getEmp_id() {
		return emp_id;
	}

	public String getEmp_name() {
		return emp_name;
	}
	
	public Integer getDept_id() {
		return dept_id;
	}
	
	public List<String> getAuthNames() {
		return authNames;
	}

	@Override
	public String toString() {
		return "AdminUserDetails [emp_id=" + emp_id + ", emp_name=" + emp_name + ", dept_id=" + dept_id + ", username=" + username
				+ ", password=" + password + ", authorities=" + authorities + ", authNames=" + authNames + "]";
	}

	
	
}
