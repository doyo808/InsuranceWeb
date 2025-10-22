package com.kd.insuranceweb.admin.service;

import java.util.List;
import java.util.Map;

public interface AdminDashboardService {

    int countSellingProducts();
    int countPendingContracts();
    int countPendingClaims();

    /**
     * 이번 달 계약 통계
     * - newContracts: 이번 달 시작된 계약 수 (START_DATE 기준)
     * - endedContracts: 이번 달 종료된 계약 수 (END_DATE 기준)
     */
    Map<String, Integer> getMonthlyContractStats();  

    /**
     * 이번 달 청구 통계
     * - totalAmount: 이번 달 총 청구금액 (만원 단위)
     * - approvedAmount: 이번 달 승인된 청구금액 (만원 단위)
     */
    Map<String, Integer> getMonthlyClaimStats();     

    List<Map<String, Object>> getRecentActivities();
}
