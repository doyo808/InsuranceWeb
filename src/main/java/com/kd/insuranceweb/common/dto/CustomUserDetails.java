package com.kd.insuranceweb.common.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	private final CustomerDTO customer;
	
	public CustomUserDetails(CustomerDTO customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return customer.getPassword_hash();
	}

	@Override
	public String getUsername() {
		return customer.getLogin_id();
	}
	
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았다고 간주
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았다고 간주
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않았다고 간주
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화되었다고 간주
    }

    public CustomerDTO getCustomer() {
        return customer;
    }
}
