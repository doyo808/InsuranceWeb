package com.kd.insuranceweb.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.dto.ContractListRowDTO;
import com.kd.insuranceweb.admin.dto.ContractSearchCriteria;
import com.kd.insuranceweb.admin.mapper.AdminContractMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

	private final AdminContractMapper mapper;

	@Override
	public List<ContractListRowDTO> findReceiptsPage(ContractSearchCriteria criteria) {
		    return mapper.findReceiptsPage(criteria); // ★ 페이징 쿼리 호출

	}

	@Override
	public int countReceipts(ContractSearchCriteria criteria) {
		return mapper.countReceipts(criteria);
	}

	
	
}
