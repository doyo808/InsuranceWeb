package com.kd.insuranceweb.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.admin.annotation.AdminActionLog;
import com.kd.insuranceweb.admin.dto.ContractDetailDTO;
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

	@Override
	public ContractDetailDTO getContractDetail(Integer contractId) {
		return mapper.findContractDetail(contractId);
	}

	@Override
	public void approveContract(Integer contractId) {
		mapper.approveContract(contractId);
	}

	@Override
	public void rejectContract(Integer contractId, String reason) {
		mapper.rejectContract(contractId, reason);
	    // 만약 reason을 DB에 저장한다면 여기에 추가 로직 작성
	}

    @Override
    public int getPendingCount() {
        return mapper.countPendingContracts();
    }
	
	
}
