package com.kd.insuranceweb.mall.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractMapper {
	/**
     * 파라미터를 Map으로 받아 계약 정보를 추가합니다.
     * 실행 후, 파라미터로 전달된 Map에 'contract_id'가 채워집니다.
     */
    int insertContract(Map<String, Object> params);
}
