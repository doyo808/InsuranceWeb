package com.kd.insuranceweb.mall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.mall.dto.InsuredDTO;

@Mapper
public interface InsuredMapper {
	 /**
     * 피보험자 정보 삽입
     * @param dto InsuredDTO
     * @return 삽입 성공 건수
     */
    int insertInsured(@Param("dto") InsuredDTO dto);

}
