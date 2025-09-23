package com.kd.insuranceweb.mypage.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingConsentDTO {
	private Integer marketingConsentId;
	private Integer customerId;
	
	// 메인 동의 항목
	private String consentCollection;
	private String consentMarketing;
	private String consentSharing;
	private String consentLookup;
	
	// 마케팅 채널
	private String channelPhone;
	private String channelSms;
	private String channelEmail;
	
	// 최종 수정일
	private LocalDateTime updatedAt;
}
