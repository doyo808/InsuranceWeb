package com.kd.insuranceweb.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.common.dto.CustomerDTO;

@Mapper
public interface CustomerMapper {
	/**
     * 로그인ID로 고객 정보를 조회합니다.
     *
     * @param loginId 조회할 사용자의 로그인 아이디
     * @return 조회된 사용자 정보. 없을 경우 null.
     */
    CustomerDTO selectById(@Param("login_id") String login_id);
    int insertCustomer(CustomerDTO customer);
}
