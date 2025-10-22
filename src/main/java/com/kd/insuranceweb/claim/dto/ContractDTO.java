package com.kd.insuranceweb.claim.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ContractDTO {
    private Integer contract_id;
    private Integer customer_id;
    private Integer product_id;
    private String product_name;
    private LocalDate start_date;
    private LocalDate end_date;
    private String status;
}
