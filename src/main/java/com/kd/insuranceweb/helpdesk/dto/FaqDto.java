package com.kd.insuranceweb.helpdesk.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//Getter/Setter, toString(), equals(), hashCode() 자동 생성
@NoArgsConstructor
//기본생성자
@AllArgsConstructor
//모든 필드 생성자
public class FaqDto {
	private Long faq_id;
	private String category;
	private String question;
	private String answer;
	private String writer;
	private Date created_at;
	private Date updated_at;
	

}