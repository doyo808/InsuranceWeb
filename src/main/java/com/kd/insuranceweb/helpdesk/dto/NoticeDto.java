package com.kd.insuranceweb.helpdesk.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//Getter/Setter, toString(), equals(), hashCode() 자동 생성
@NoArgsConstructor
//기본생성자
@AllArgsConstructor
//모든 필드 생성자
public class NoticeDto {
	private Long notice_id;
	private String title;
	private String content;
	private String writer;
	private String is_visible;	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
	private Date created_at;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
	private Date updated_at;

}