package com.kd.insuranceweb.common.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private PersonDTO person;
    private CustomerDTO customer;
    private final Integer customer_id;
    private final Integer person_id;
    private final String login_id;
    private final String password_hash;

    public CustomUserDetails(Integer customerId, Integer personId, String loginId, String passwordHash) {
        this.customer_id = customerId;
        this.person_id = personId;
        this.login_id = loginId;
        this.password_hash = passwordHash;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한은 임시로 빈 리스트 반환
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password_hash;
    }

    @Override
    public String getUsername() {
        return login_id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return true; 
    }
}
