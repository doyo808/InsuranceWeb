package com.kd.insuranceweb.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.admin.dto.ContractListRowDTO;
import com.kd.insuranceweb.admin.dto.ContractSearchCriteria;

@Mapper
public interface AdminContractMapper {
	    // 필요하면 남겨두되, 페이징에는 이 메서드가 사용되어야 함
	    // List<ContractListRowDTO> findContractList();

	    int countReceipts(ContractSearchCriteria criteria);
	    // ★ 추가: 페이징용
	    List<ContractListRowDTO> findReceiptsPage(ContractSearchCriteria criteria);

}
