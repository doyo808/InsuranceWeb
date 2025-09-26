package com.kd.insuranceweb.mall.service;

import org.springframework.stereotype.Service;

//import com.kd.insuranceweb.mall.vo.mapper.PremiumRateMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MallService {
	/***
	 * 여기서 DB로부터 조건을 비교해서 
	 * 가입자에게 적당한 보험료를 계산해준다
	 * @return 보험료
	 */
	
//	private final PremiumRateMapper premiumMapper;
	
	public double getPremiumVal() {
		
		return 1.1;
	}
}
