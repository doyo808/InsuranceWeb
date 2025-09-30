package com.kd.insuranceweb.admin.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClaimListRowDTO {
 private Integer claim_id;          // CLAIM_ID
 private String person_name;   // BENEFICIARY_NAME
 private String claim_type;      // CLAIM_TYPE
 private LocalDate accident_date;// ACCIDENT_DATE
 private LocalDate claim_date;   // CLAIM_DATE
 private Integer claim_status;         // CLAIM_STATUS
}
