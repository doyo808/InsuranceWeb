package com.kd.insuranceweb.mall.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kd.insuranceweb.admin.service.ProductServiceImpl;
import com.kd.insuranceweb.mall.model.dto.ProductRequestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping
    public ResponseEntity<?> registerProduct(
            @RequestPart("requestData") ProductRequestDTO requestData,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "conditions", required = false) MultipartFile conditions) {

    	System.out.println("product api 입니다");
    	System.out.println(requestData);
        try {
            // 서비스에서 db에 대한 처리를함
            productService.registerProduct(requestData, thumbnail, conditions);
            return ResponseEntity.ok(Map.of("message", "상품 등록 완료"));
        } catch (Exception e) {
//            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
