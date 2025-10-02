package com.kd.insuranceweb.mall.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoverageCategoryMapper {
	int insert();
	int select();
	int update();
	int delete();
}
