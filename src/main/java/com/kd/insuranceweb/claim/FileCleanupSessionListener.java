package com.kd.insuranceweb.claim;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;


// 세션만료시 임시파일 삭제함
@Component
public class FileCleanupSessionListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        List<String> uploadedFiles = (List<String>) se.getSession().getAttribute("uploadedFiles");
        if (uploadedFiles != null) {
            for (String fileName : uploadedFiles) {
                File file = new File("C:/javaweb_yhs/InsuranceWebUploadedFiles/claims/temp/" + fileName);
                if (file.exists()) file.delete();
                
                
            }
        }
    }
}


