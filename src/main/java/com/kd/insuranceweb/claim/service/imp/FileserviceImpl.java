package com.kd.insuranceweb.claim.service.imp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kd.insuranceweb.claim.service.FileService;

import jakarta.servlet.http.HttpSession;

@Service
public class FileserviceImpl implements FileService {

    private final String tempDir = "C:/javaweb_yhs/InsuranceWebUploadedFiles/claims/temp/";
    private final String finalDir = "C:/javaweb_yhs/InsuranceWebUploadedFiles/claims/";

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "application/pdf",
        "image/png",
        "image/jpeg"
    );

    private static final long SIZE_MAX = 3 * 1024 * 1024; // 3MB

    @Override
    public List<String> handleFileUpload(MultipartFile receipt, MultipartFile details, MultipartFile etc) throws IOException {
        if (receipt.isEmpty() || details.isEmpty()) {
            throw new IOException("필수 파일이 누락되었습니다.");
        }

        validateFile(receipt);
        validateFile(details);
        if (etc != null && !etc.isEmpty()) validateFile(etc);

        List<String> uploadedFiles = new ArrayList<>();
        uploadedFiles.add(saveFileToTemp(receipt));
        uploadedFiles.add(saveFileToTemp(details));
        if (etc != null && !etc.isEmpty()) {
            uploadedFiles.add(saveFileToTemp(etc));
        }

        return uploadedFiles;
    }

    @Override
    public void cancelUpload(List<String> uploadedFiles) {
        if (uploadedFiles != null) {
            for (String fileName : uploadedFiles) {
                File file = new File(tempDir + fileName);
                if (file.exists()) file.delete();
            }
        }
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.getSize() > SIZE_MAX) {
            throw new IOException("파일 크기가 3MB를 초과했습니다: " + file.getOriginalFilename());
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new IOException("허용되지 않은 파일 형식입니다: " + contentType);
        }
    }

    private String saveFileToTemp(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString() + ext;

        File dest = new File(tempDir, savedName);
        file.transferTo(dest);
        return savedName;
    }
    
    // ✅ temp → finalDir로 이동
    public List<String> moveFilesToFinal(HttpSession session) throws IOException {
        @SuppressWarnings("unchecked")
        List<String> uploadedFiles = (List<String>) session.getAttribute("uploadedFiles");

        if (uploadedFiles == null || uploadedFiles.isEmpty()) {
            throw new IOException("세션에 업로드된 파일이 없습니다.");
        }

        List<String> movedFiles = new ArrayList<>();

        for (String savedName : uploadedFiles) {
            Path sourcePath = Path.of(tempDir, savedName);
            Path targetPath = Path.of(finalDir, savedName);

            // finalDir 폴더 없으면 생성
            Files.createDirectories(targetPath.getParent());

            // 파일 이동 (기존에 있으면 덮어쓰기)
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            movedFiles.add(targetPath.toString());
        }

        // ✅ 세션에서 임시 파일 목록 제거 (선택사항)
        session.removeAttribute("uploadedFiles");

        return movedFiles; // 최종 저장된 경로들 반환
    }


}