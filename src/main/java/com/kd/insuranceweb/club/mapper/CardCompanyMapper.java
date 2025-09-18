package com.kd.insuranceweb.club.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CardCompanyMapper {
	String selectPhoneByName(@Param("card_company_name") String card_company_name);
}
