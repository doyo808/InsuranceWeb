package com.kd.insuranceweb.admin.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClaimSearchCriteria extends CommonSearchCriteria {
	private String q;                 // 검색어(선택)
    private String claimType;         // 청구유형(선택)
    private Integer status;           // 처리상태(선택)
    private LocalDate from;           // 시작일(yyyy-MM-dd)
    private LocalDate to;             // 종료일(yyyy-MM-dd)
    
    // 컨트롤러에서 계산해서 넣어줄 필드
    private LocalDate toExclusive;    // = to.plusDays(1)
    
    private int page = 1;   // 현재 페이지
    private int size = 8;   // 페이지당 개수

    public int getOffset() { return (page - 1) * size; }
}
