package com.kd.insuranceweb.mall.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.mall.vo.PremiumRateRow;
import com.kd.insuranceweb.mall.vo.mapper.PremiumRateRowMapper;

//import com.kd.insuranceweb.mall.vo.mapper.PremiumRateMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MallService {
	
	private final PremiumRateRowMapper rateMapper;
	/***
	 * 여기서 DB로부터 조건을 비교해서 
	 * 가입자에게 적당한 보험료를 계산해준다
	 * 
	 * 값이 비거나 문제가 생기면 -1을 리턴한다
	 * @return 보험료
	 */
	public double getPremiumRate(int id, String birth, String gender, String job) {

		// 현재 년도를 참조해서 나이를 계산한다
		int age = (Calendar.getInstance().get(Calendar.YEAR) 
				- Integer.parseInt(birth.substring(0, 4)));

		// 보험료 계산 공식
		// 보험료 = 기본보험료 × (1+W성별​+W연령대​)
		// 아래 식은 일단은 보류
		// × (보장금액 / 기준 보장금액​ + (자기부담금 가산))
		
		Map<String, Object> params = new HashMap<>();
		params.put("product_id", id);
		params.put("gender", gender);
		params.put("age", age);
		
		// 📄 premium_rate_rows – 요율 조건별 데이터
		// 위 테이블을 참조해서 조건을 검색하는 것을 만들자
		// 검색된 데이터에서 기본요율을 받아오면
		// 받아온 요율과 기본보험료를 곱해서 값을 돌려주자
		// base_rate[📄 premium_rate_rows] * base_premium[📄coverage_definitions]
		PremiumRateRow rate = rateMapper.getRate(params);
		if(rate != null) {
			System.out.println("DB로부터 정보를 받아옴 : " + rate.toString());
			
			return (1 + rate.getAge_weight() + rate.getGender_weight());
		} else {
			return -1;
		}
	}
}
