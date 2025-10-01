package com.kd.insuranceweb.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.common.dto.PersonDTO;

@Mapper
public interface PersonMapper {
	/**
     * 이메일로 사용자 정보를 조회합니다.
     *
     * @param email 조회할 사용자의 이메일
     * @return 조회된 사용자 정보. 없을 경우 null.
     */
    PersonDTO selectByEmail(@Param("email") String email);
    PersonDTO selectById(@Param("person_id") Integer person_id);
    
    /**
     * PersonDTO로 DB에 행을 삽입합니다.
     * 삽입된 행의 pk(person_id)값을 채운 person객체를 갖고있습니다.
     * @param person
     * @return 삽입결과(성공시 1)
     */
    int insertPerson(PersonDTO person);
    int updatePerson(@Param("email") String email,
    				 @Param("phone_number") String phone_number,
    				 @Param("person_id") Integer person_id);
}
