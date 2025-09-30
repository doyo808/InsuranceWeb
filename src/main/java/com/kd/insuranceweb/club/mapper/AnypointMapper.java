package com.kd.insuranceweb.club.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.club.dto.AnypointTransaction;

@Mapper
public interface AnypointMapper {
	List<AnypointTransaction> selectTxns(@Param("customerId") Long customerId, @Param("start") LocalDate start,
			@Param("end") LocalDate end);

	Long selectBalance(@Param("customerId") Long customerId);
}
