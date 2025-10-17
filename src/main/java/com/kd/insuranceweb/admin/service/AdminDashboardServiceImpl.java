package com.kd.insuranceweb.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.mapper.AdminDashboardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final AdminDashboardMapper mapper;

    @Override
    public int countSellingProducts() {
        return mapper.countSellingProducts();
    }

    @Override
    public int countPendingContracts() {
        return mapper.countPendingContracts();
    }

    @Override
    public int countPendingClaims() {
        return mapper.countPendingClaims();
    }

    @Override
    public Map<String, Integer> getMonthlyContractStats() {
        return Map.of(
            "newContracts", mapper.countNewContractsThisMonth(),
            "endedContracts", mapper.countEndedContractsThisMonth() // ✅ 수정된 부분
        );
    }

    @Override
    public Map<String, Integer> getMonthlyClaimStats() {
        return Map.of(
            "totalAmount", mapper.sumClaimsThisMonth(),
            "approvedAmount", mapper.sumApprovedClaimsThisMonth()
        );
    }

    @Override
    public List<Map<String, Object>> getRecentActivities() {
        return mapper.selectRecentActivities();
    }
}
