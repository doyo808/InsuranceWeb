package com.kd.insuranceweb.mypage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.mypage.dto.ContractDto;

@Mapper
public interface MyContractMapper {
	List<ContractDto> selectAllContracts();
	List<ContractDto> selectActiveContracts();
}
