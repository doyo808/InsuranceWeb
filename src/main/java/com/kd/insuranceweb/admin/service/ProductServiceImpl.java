package com.kd.insuranceweb.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.kd.insuranceweb.admin.dto.ProductListRowDTO;
import com.kd.insuranceweb.admin.dto.ProductSearchCriteria;
import com.kd.insuranceweb.admin.mapper.AdminProductMapper;
import com.kd.insuranceweb.mall.mapper.ProductMapper;
import com.kd.insuranceweb.mall.model.dto.ProductRequestDTO;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	 private final AdminProductMapper mapper;
	 private final ProductMapper productMapper;
	
	 @Override
	 public int countProducts(ProductSearchCriteria criteria) {
	     Map<String,Object> params = new HashMap<>();
	     params.put("criteria", criteria);
	     return mapper.countProducts(params);
	 }
	
	 @Override
	 public List<ProductListRowDTO> findProductPage(ProductSearchCriteria criteria) {
	     Map<String,Object> params = new HashMap<>();
	     params.put("criteria", criteria);
	     params.put("startRow", criteria.getStartRow());
	     params.put("endRow",   criteria.getEndRow());
	     return mapper.findProductPage(params);
	 }
	
	 @Override
	 public int countProductsOnSale() {
		return mapper.countProductsOnSale();
	 }
	 @Transactional
	 public void registerProduct(ProductRequestDTO dto) {
		 
		 System.out.println("db에 상품 등록중");
		// 1) 상품 insert -> dto.productId에 시퀀스값 채워짐
	     productMapper.insertInsuranceProduct(dto);
	     // 나중에 id값이 필요할때 사용
	     Long productId = dto.getProductId();
	
	     // 2) 담보 insert all (만약 coverages가 null/empty면 호출하지 않음)
	     if (dto.getCoverages() != null && !dto.getCoverages().isEmpty()) {
	         // productId는 mapper에서 #{productId}로 사용하므로 dto에 그대로 둠
	         productMapper.insertCoverageDefinitions(dto);
	     }
	
	     // 3) 요율 insert all
	     if (dto.getPremiumRates() != null && !dto.getPremiumRates().isEmpty()) {
	         productMapper.insertPremiumRateRows(dto);
	     }
	 }
}
