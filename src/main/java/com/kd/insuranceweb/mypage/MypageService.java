package com.kd.insuranceweb.mypage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kd.insuranceweb.common.dto.CustomerDTO;
import com.kd.insuranceweb.common.mapper.CustomerMapper;
import com.kd.insuranceweb.common.mapper.PersonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {
	
	private final PersonMapper personMapper;
	private final CustomerMapper customerMapper;
	
	@Transactional
	public int EditPersonAndCustomer(CustomerDTO customer) {
		int updatedPersonRows = EditPerson(customer);
		int updatedCustomerRows = EditCustomer(customer);
		return updatedPersonRows + updatedCustomerRows;
	}
	
	
	private int EditPerson(CustomerDTO customer) {
		return personMapper.updatePerson(
								customer.getEmail(),
								customer.getPhone_number(), 
								customer.getPerson_id());
	}
	private int EditCustomer(CustomerDTO customer) {
		return customerMapper.updateCustomer(
								customer.getZip_code(),
								customer.getAddress_1(),
								customer.getAddress_2(),
								customer.getHome_number(),
								customer.getJob(),
								customer.getCompany_name(),
								customer.getJob_zip_code(),
								customer.getJob_address1(),
								customer.getJob_address2(),
								customer.getJob_phone_number(),
								customer.getCustomer_id());
	}
	
	
}
