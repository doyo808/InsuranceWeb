package com.kd.insuranceweb.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDashboardMapper {

	int countSellingProducts();
    int countPendingContracts();
    int countPendingClaims();

    int countNewContractsThisMonth();
    int countEndedContractsThisMonth(); 
    int sumClaimsThisMonth();
    int sumApprovedClaimsThisMonth();

    List<Map<String, Object>> selectRecentActivities();
}
