package com.kd.insuranceweb.admin.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UiPathNoticeDto {
	Integer id;
	String title;
	String content;
	LocalDateTime created;
}
