package com.kd.insuranceweb.mall.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductVO {
	private Long productId;
	private String product_type;
    private String product_name;
    private String product_desc;
    private Date start_date;
    private Date end_Date;
    private String thumbnail;
    private String conditions;
    private Date created_at;
    private Date updated_at;
}
