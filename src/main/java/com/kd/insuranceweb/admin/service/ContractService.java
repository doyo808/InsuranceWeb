package com.kd.insuranceweb.admin.service;

import java.util.List;

import com.kd.insuranceweb.admin.dto.ContractDetailDTO;
import com.kd.insuranceweb.admin.dto.ContractListRowDTO;
import com.kd.insuranceweb.admin.dto.ContractSearchCriteria;

public interface ContractService {

	int countReceipts(ContractSearchCriteria criteria);

	List<ContractListRowDTO> findReceiptsPage(ContractSearchCriteria criteria);

	ContractDetailDTO getContractDetail(Integer contractId);
	
	void approveContract(Integer contractId);
	void rejectContract(Integer contractId, String reason);

	int getPendingCount();   // 접수(PENDING) 건수
}
