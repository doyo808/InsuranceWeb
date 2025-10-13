package com.kd.insuranceweb.admin.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class ProductSearchCriteria {
 private String q;          // 상품명/ID 검색
 private String status;     // = product_types (car, driver, ...)

 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 private LocalDate from;    // 판매시작일(From)

 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 private LocalDate to;      // 판매시작일(To)

 // 페이징
 private int page = 1;
 private int size = 8;

 public int getOffset()  { return (page - 1) * size; }
 public int getStartRow(){ return getOffset() + 1; }
 public int getEndRow()  { return getOffset() + size; }
}

