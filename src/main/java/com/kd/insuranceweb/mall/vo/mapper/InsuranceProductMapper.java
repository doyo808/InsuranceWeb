package com.kd.insuranceweb.mall.vo.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsuranceProductMapper {
	int select();
	int update();
	int insert();
	int delete();
}
