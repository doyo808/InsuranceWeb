package com.kd.insuranceweb.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.kd.insuranceweb.admin.dto.ProductListRowDTO;
import com.kd.insuranceweb.admin.dto.ProductSearchCriteria;
import com.kd.insuranceweb.admin.mapper.AdminProductMapper;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

 private final AdminProductMapper mapper;

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
}
