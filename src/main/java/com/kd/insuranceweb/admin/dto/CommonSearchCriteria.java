package com.kd.insuranceweb.admin.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class CommonSearchCriteria {
	private String q; // 키워드
	private Integer status; // 상태(선택)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate from; // 시작일(선택)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate to; // 종료일(선택)

}

