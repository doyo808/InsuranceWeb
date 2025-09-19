package com.kd.insuranceweb.club.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.club.dto.Card_benefit_interest_free;

@Mapper
public interface CardBenefitMapper {
	List<Card_benefit_interest_free> selectBenefitInterestFree(@Param("card_company_name") String cardCompanyName);
}
