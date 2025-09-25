package com.kd.insuranceweb.mypage.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingConsentDTO {
	private Integer marketing_consent_id;
	private Integer customer_id;
	
	// 메인 동의 항목
	private String consent_collection;
	private String consent_marketing;
	private String consent_sharing;
	private String consent_lookup;
	
	// 마케팅 채널
	private String channel_phone;
	private String channel_sms;
	private String channel_email;
	
	// 최종 수정일
	private LocalDateTime updated_at;
}
