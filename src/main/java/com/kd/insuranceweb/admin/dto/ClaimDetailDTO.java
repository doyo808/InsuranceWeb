package com.kd.insuranceweb.admin.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClaimDetailDTO { 
	
	// claims table
  private Integer claim_id; // 청구ID
  private String  claim_type;  // 청구유형
  private String  compensation_type; // 보상구분(인: H/물: P)
  private String  accident_description; // 사고내용
  private Date    accident_date; //사고/발병일 
  private Date    claim_date; // 청구(접수)날짜
  
  private Integer claim_status; // 청구상태 (1, 2, 3)
  
  private String diagnosis_cd; // 질병 코드
  
  private String beneficiary_name; // 수익자 이름
  private String beneficiary_email; // 수익자 이메일
  private String bank_name; // 은행명
  private String bank_account; //계좌번호
  private String beneficiary_postcode; // 수익자 우편번호
  private String beneficiary_address; // 수익자 주소
  private Long total_paid_amount; // 총 지급금액(null이 나오기도 함)
  private Date completion_date; // 종결 날짜 (null이 나오기도 함)
  
  private String detail_file_path; // 진료비 내역서 경로
  private String receipt_file_path; // 영수증 파일 경로
  private String etc_file_path; // 기타파일 경로
  
  // contract table
  private String  contract_id; // 계약 ID
  
  // contract table -> product_plans table -> products table
  private String  product_name;
  
  // insureds table -> persons table
  private String person_name;  // 피보험자 이름
  private String phone_number; // 피보험자 연락처

  
  

}

