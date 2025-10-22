package com.kd.insuranceweb.admin.dto;

import lombok.Data;

@Data
public class DashboardStats {
	private int newContracts;
	private int endedContracts;
	private int totalClaims;
	private int approvedClaims;
}
