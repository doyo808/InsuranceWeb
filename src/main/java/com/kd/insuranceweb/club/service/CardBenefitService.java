package com.kd.insuranceweb.club.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.club.dto.Card_benefit_interest_free;
import com.kd.insuranceweb.club.mapper.CardBenefitMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardBenefitService {
	
	private final CardBenefitMapper cardBenefitMapper;
	
	// 인자값으로 @RequestParam("card_company_name")이 필요할 수도..?!?
	public List<Card_benefit_interest_free> selectBenefitInterestFree(String cardCompanyName) {
	
		return cardBenefitMapper.selectBenefitInterestFree(cardCompanyName);
	}
}
