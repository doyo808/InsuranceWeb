package com.kd.insuranceweb.claim.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

public interface FileService {
    List<String> handleFileUpload(MultipartFile receipt, MultipartFile details, MultipartFile etc) throws IOException;
    void cancelUpload(List<String> uploadedFiles);
    List<String> moveFilesToFinal(HttpSession session) throws IOException; // 추가
}
