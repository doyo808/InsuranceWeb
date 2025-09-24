package com.kd.insuranceweb.claim;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor   // 기본 생성자
@AllArgsConstructor  // 모든 필드를 받는 생성자
public class Claim {
    private Long claimId;
    private Long contractId;
    private Long customerId;
    private String claimType;
    private Date claimDate;
    private Date accidentDate;
    private String compensationType;
    private String diagnosisCd;
    private String accidentDescription;
    private String beneficiaryName;
    private String bankAccount;
    private String bankName;
    private String email;
    private Integer claimStatus;
    private Date completionDate;
    private Long totalPaidAmount;
    private String detailFilePath;
    private String receiptFilePath;
    private String etcFilePath;
    private String documentStatus;

    // getter/setter
}