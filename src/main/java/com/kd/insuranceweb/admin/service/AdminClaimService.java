package com.kd.insuranceweb.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.kd.insuranceweb.admin.dto.ClaimListRowDTO;
import com.kd.insuranceweb.admin.mapper.AdminClaimMapper;

@Service
@RequiredArgsConstructor
public class AdminClaimService {
 private final AdminClaimMapper adminClaimMapper;

 public List<ClaimListRowDTO> getClaimList() {
     return adminClaimMapper.findClaimList();
 }
}
