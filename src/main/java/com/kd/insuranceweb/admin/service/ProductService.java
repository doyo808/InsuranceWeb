package com.kd.insuranceweb.admin.service;

import java.util.List;

import com.kd.insuranceweb.admin.dto.ProductListRowDTO;
import com.kd.insuranceweb.admin.dto.ProductSearchCriteria;

public interface ProductService {

	int countProducts(ProductSearchCriteria criteria);
    List<ProductListRowDTO> findProductPage(ProductSearchCriteria criteria);
    
    int countProductsOnSale();
}
