package com.kd.insuranceweb.mall.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.mall.model.PremiumRateRow;

@Mapper
public interface PremiumRateRowMapper {
	PremiumRateRow getRate(Map<String, Object> params);
	List<PremiumRateRow> getList();
}
