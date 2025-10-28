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
     * - newClaims: 이번 달 접수된 신규청구 건 (START_DATE 기준)
     * - endClaims: 이번 달 지급완료된 청구 건 (END_DATE 기준)
     */
    Map<String, Object> getClaimStats();  

    List<Map<String, Object>> getRecentActivities();
}
