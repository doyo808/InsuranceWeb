package com.kd.insuranceweb.admin.dto;

import lombok.Data;

@Data
public class DashboardStats {
    private int newContracts;       // 이번 달 신규 계약 수
    private int cancelledContracts; // 이번 달 해지 계약 수
    private int totalClaimAmount;   // 이번 달 총 청구 금액 (만원 단위)
    private int paidClaimAmount;    // 이번 달 지급 완료 금액 (만원 단위)
}
