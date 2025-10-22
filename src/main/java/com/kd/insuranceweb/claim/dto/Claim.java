package com.kd.insuranceweb.claim.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor   // 기본 생성자
@AllArgsConstructor  // 모든 필드를 받는 생성자
public class Claim {
	 private Integer claim_id;
	    private Integer customer_id;
	    private Integer person_id;
	    private Integer contract_id;

	    private String claim_type;
	    private Date claim_date;
	    private Date accident_date;
	    private String accident_description;

	    private String beneficiary_name;
	    private String bank_account;
	    private String bank_name;
	    private String beneficiary_email;

	    private Integer claim_status;
	    private Date completion_date;
	    private Long total_paid_amount;

	    private String detail_file_path;
	    private String receipt_file_path;
	    private String etc_file_path;

	    private String beneficiary_postcode;
	    private String beneficiary_address;
	    private String medical_benefits;
	    private String accident_type;
	    private String document_status;
}