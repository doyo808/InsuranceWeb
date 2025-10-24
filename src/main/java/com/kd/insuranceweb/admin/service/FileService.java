package com.kd.insuranceweb.admin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final Path tempDir = Paths.get("uploads/temp");

    // 임시 디렉토리에 잠깐 저장해둠
    public String saveTempFile(MultipartFile file) throws IOException {
        Files.createDirectories(tempDir);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = tempDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

    // db에 무사히 데이터가 전달되었을때 임시저장한 파일을 옮겨놈
    public void moveToProductDir(String tempFileUrl, String dir) throws IOException {
        String fileName = Paths.get(tempFileUrl).getFileName().toString();
        Path uploadDir = Paths.get("uploads", dir);
        Path source = tempDir.resolve(fileName);
//        Path target = uploadDir.resolve(fileName);
        Files.createDirectories(uploadDir);
        Files.move(source, uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    // 파일이 무사히 옮겨졌을때 임시저장된 데이터를 지워줌
    public void deleteTempFile(String tempFileUrl) throws IOException {
        String fileName = Paths.get(tempFileUrl).getFileName().toString();
        Files.deleteIfExists(tempDir.resolve(fileName));
    }
}

