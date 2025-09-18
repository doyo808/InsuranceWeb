package com.kd.insuranceweb.club.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.kd.insuranceweb.club.mapper.CardCompanyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardCompanyService {
	
	private final CardCompanyMapper cardCompanyMapper;
	
	// 인자값으로 @RequestParam("card_company_name")이 필요할 수도..?!?
	public String selectPhoneByName(String card_company_name) {
		String phone_number = cardCompanyMapper.selectPhoneByName(card_company_name);
		return phone_number;
	}
}
