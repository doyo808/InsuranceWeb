package com.kd.insuranceweb.mall.vo.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoverageCategoryMapper {
	int insert();
	int select();
	int update();
	int delete();
}
