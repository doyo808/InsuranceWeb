package com.kd.insuranceweb.mall.vo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.mall.vo.PremiumRate;

@Mapper
public interface PremiumRateMapper {
	PremiumRate calulateVal();
}
