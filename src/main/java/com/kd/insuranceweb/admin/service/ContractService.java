package com.kd.insuranceweb.admin.service;

import java.util.List;

import com.kd.insuranceweb.admin.dto.ContractListRowDTO;
import com.kd.insuranceweb.admin.dto.ContractSearchCriteria;

public interface ContractService {

	int countReceipts(ContractSearchCriteria criteria);

	List<ContractListRowDTO> findReceiptsPage(ContractSearchCriteria criteria);

}
