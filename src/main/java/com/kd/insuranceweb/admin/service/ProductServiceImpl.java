package com.kd.insuranceweb.admin.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.kd.insuranceweb.admin.dto.ProductListRowDTO;
import com.kd.insuranceweb.admin.dto.ProductSearchCriteria;
import com.kd.insuranceweb.admin.mapper.AdminProductMapper;
import com.kd.insuranceweb.mall.mapper.ProductMapper;
import com.kd.insuranceweb.mall.model.dto.CoverageDto;
import com.kd.insuranceweb.mall.model.dto.PremiumRateDto;
import com.kd.insuranceweb.mall.model.dto.ProductRequestDTO;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	 private final AdminProductMapper mapper;
	 private final ProductMapper productMapper;
	 private final FileService fileService;
	
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
	 public void registerProduct(ProductRequestDTO dto, MultipartFile thumb, MultipartFile con) {
		 try {
			 // (1) DB에 등록
			 
			 // 임시로 파일들 업로드 해주고 이름 겹치지않게 만들어줌
			 dto.setThumbnail(fileService.saveTempFile(thumb));
			 dto.setConditions(fileService.saveTempFile(con));
			 
			 // 1) 상품 insert -> dto.productId에 시퀀스값 채워짐
			 
			 productMapper.insertInsuranceProduct(dto);
			 // 나중에 id값이 필요할때 사용
			 Long productId = dto.getProductId();
			 // 전달받은 담보,요율 데이터들
			 List<CoverageDto> cover = dto.getCoverages();
			 List<PremiumRateDto> premium = dto.getPremiumRates();
			 // 2) 담보 insert all
			 // productId는 mapper에서 #{productId}로 사용하므로 dto에 그대로 둠
			 for (CoverageDto c : cover) {
				 c.setProduct_id(productId);
				 productMapper.insertCoverageDefinition(c);
			 }
			 
			 // 3) 요율 insert all
			 for (PremiumRateDto p : premium) {
				 p.setProduct_id(productId);
				 productMapper.insertPremiumRateRow(p);	    		 
			 }
			 
			 // (2) 트랜잭션 커밋 후 실행할 작업 등록
			 // 임시로 업로드된 파일들 커밋성공시 제대로 업로드를해줌
			 // 실패시 업로드된 임시파일 삭제
			 TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				 @Override
				 public void afterCommit() {
					 try {
						 fileService.moveToProductDir(dto.getThumbnail(), "thumbnails");
						 fileService.moveToProductDir(dto.getConditions(), "conditions");
					 } catch (IOException e) {
						 // 로깅/알림 처리
						 e.printStackTrace();
					 }
				 }
			 });
		 } catch (IOException ioe) {
			System.out.println("파일업로드중 문제가 생김" + ioe);
		 } catch (Exception e) {
			// (3) DB 실패 시 임시 파일 삭제
            try {
                fileService.deleteTempFile(dto.getThumbnail());
                fileService.deleteTempFile(dto.getConditions());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw e; // 트랜잭션 롤백
		 }
	     
	 }
}
