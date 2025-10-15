package com.kd.insuranceweb.common.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminUserDetails implements UserDetails, LoginUser {

	private Integer emp_id;
	private String emp_name;
	private String username;
	private String password;
	private List<GrantedAuthority> authorities;
	private List<String> authNames;
	
	
	public AdminUserDetails(Integer emp_id, String emp_name, String username, String password,
			List<GrantedAuthority> authorities, List<String> authNames) {
		super();
		this.emp_id = emp_id;
		this.emp_name = emp_name;
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

	
	public List<String> getAuthNames() {
		return authNames;
	}

	@Override
	public String toString() {
		return "AdminUserDetails [emp_id=" + emp_id + ", emp_name=" + emp_name + ", username=" + username
				+ ", password=" + password + ", authorities=" + authorities + ", authNames=" + authNames + "]";
	}

	
	
}
