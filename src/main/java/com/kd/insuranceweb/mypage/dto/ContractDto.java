package com.kd.insuranceweb.mypage.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContractDto {
    private Integer contract_id;
    private String product_name;
    private String customer_name;
    private String insured_name;
    private Date start_date;
    private Date end_date;
    private Integer total_premium;
    private String status;
}
