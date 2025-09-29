package com.kd.insuranceweb.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.dto.ClaimSearchCriteria;
import com.kd.insuranceweb.admin.mapper.AdminClaimMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminClaimService {
	private final AdminClaimMapper adminClaimMapper;

    public List<ClaimListRowDTO> findClaimList(ClaimSearchCriteria criteria) {
        return adminClaimMapper.findClaimList(criteria);
 }
}
