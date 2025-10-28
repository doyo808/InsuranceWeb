package com.kd.insuranceweb.mall.model.dto;

import java.util.List;

import lombok.Data;

//ProductDto.java
@Data
public class ProductDTO {
 private Long product_id;
 private String product_name;
 private String product_desc;
 private String thumbnail;

 private List<CoverageDto> coverages; // 1:N 관계
}
