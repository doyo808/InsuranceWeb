package com.kd.insuranceweb.mypage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.mypage.dto.MarketingConsentDTO;

@Mapper
public interface MarketingConsentMapper {
    /**
     * customer_id로 마케팅 동의 정보 조회
     * @param customer_id 고객 ID
     * @return MarketingConsentDTO
     */
    MarketingConsentDTO selectByCustomerId(@Param("customer_id") Integer customer_id);

    /**
     * 새로운 마케팅 동의 정보 삽입
     * @param dto MarketingConsentDTO
     * @return 삽입 성공 건수
     */
    int insertMarketingConsent(@Param("dto") MarketingConsentDTO dto);

    /**
     * 기존 마케팅 동의 정보 업데이트
     * PK인 marketing_consent_id 기준
     * @param dto MarketingConsentDTO
     * @return 업데이트 성공 건수
     */
    int updateMarketingConsent(@Param("dto") MarketingConsentDTO dto);

}
