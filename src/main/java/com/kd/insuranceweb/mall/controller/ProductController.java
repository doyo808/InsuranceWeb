package com.kd.insuranceweb.mall.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
            @RequestPart("productJson") ProductRequestDTO productJson,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "conditions", required = false) MultipartFile conditions) {

    	System.out.println("product api 입니다");
        try {
            // 파일 저장(예: uploads/...), 경로를 DTO에 세팅
            String thumbPath = saveFile(thumbnail, "thumbnails");
            String condPath = saveFile(conditions, "conditions");

            productJson.setThumbnailPath(thumbPath);
            productJson.setConditionsPath(condPath);

            productService.registerProduct(productJson);
            return ResponseEntity.ok(Map.of("message", "상품 등록 완료"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private String saveFile(MultipartFile file, String dir) throws IOException {
    	System.out.println("파일업로드중");
        if (file == null || file.isEmpty()) return null;
        Path uploadDir = Paths.get("uploads", dir);
        Files.createDirectories(uploadDir);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }
}
