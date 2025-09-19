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
    CustomerDTO selectByLoginId(@Param("login_id") String login_id);
    int insertCustomer(CustomerDTO customer);
    int updateCustomer(@Param("zip_code") String zip_code,
            @Param("address_1") String address_1,
            @Param("address_2") String address_2,
            @Param("home_number") String home_number,
            @Param("job") String job,
            @Param("company_name") String company_name,
            @Param("job_zip_code") String job_zip_code,
            @Param("job_address1") String job_address1,
            @Param("job_address2") String job_address2,
            @Param("job_phone_number") String job_phone_number,
            @Param("customer_id") Integer customer_id);
}
