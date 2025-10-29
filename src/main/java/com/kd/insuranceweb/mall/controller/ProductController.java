package com.kd.insuranceweb.mall.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kd.insuranceweb.admin.service.ProductServiceImpl;
import com.kd.insuranceweb.mall.model.dto.ProductDTO;
import com.kd.insuranceweb.mall.model.dto.ProductRequestDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping("/get")
    public ResponseEntity<?> getProductData(HttpSession session) {
    	// 세션에서 상품 id를 가져온다
    	Long id = Long.parseLong(session.getAttribute("productId").toString());
    	// 서비스에서 id로 db로부터 필요한 값을 받아온 후 상품계산 페이지에서 사용할 DTO에 매핑
    	ProductDTO products = productService.getProduct(id);
    	// DB로부터 받아온 DTO로 클라이언트에 전달
    	return ResponseEntity.ok(products);
    };
    
    @PostMapping
    public ResponseEntity<?> registerProduct(
            @RequestPart("requestData") ProductRequestDTO requestData,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "conditions", required = false) MultipartFile conditions) {
        try {
            // 서비스에서 db에 대한 처리를함
        	if(thumbnail != null && conditions != null) {
        		// 데이터가 제대로 들어있을경우
                productService.registerProduct(requestData, thumbnail, conditions);
        	} else {
        		// 첨부 파일이 없을경우 테스트
        		// 비어있는 담보나 파일이 있어도 NullPointerExceptin을 발생시키지않음
        		productService.registerProductTest(requestData);
        	}
            return ResponseEntity.created(URI.create("/api/products/")).body(Map.of("message", "상품 등록 완료"));
        } catch (NullPointerException ex1) {
        	// 위의 예외상황을 테스트용으로 if로 분기해놔서 작동하지 않는코드
        	return ResponseEntity.status(410).body(Map.of("blankForm", "입력된 폼에 비어있는 값이 있습니다"));
        } catch (Exception e) {
//            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
