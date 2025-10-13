package com.kd.insuranceweb.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.admin.dto.ProductListRowDTO;

@Mapper
public interface AdminProductMapper {
	int countProducts(Map<String, Object> params);
    List<ProductListRowDTO> findProductPage(Map<String, Object> params);
    
    int countProductsOnSale();
}
