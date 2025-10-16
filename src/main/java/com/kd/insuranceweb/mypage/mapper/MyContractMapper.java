package com.kd.insuranceweb.mypage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.mypage.dto.ContractDto;

@Mapper
public interface MyContractMapper {
	List<ContractDto> selectAllContracts(@Param("customer_id") Integer customer_id);
	List<ContractDto> selectActiveContracts();
}
