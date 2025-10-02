package com.kd.insuranceweb.claim.service;

import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class OcrSample {
    public static void main(String[] args) throws IOException {
        // 이미지 파일 로드
        String filePath = "C:/javaweb_yhs/InsuranceWebUploadedFiles/claims/1.png";
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        // Vision API 요청
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);
        
        
        
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            AnnotateImageResponse response = client.batchAnnotateImages(requests).getResponses(0);
            if (response.hasError()) {
                System.out.println("Error: " + response.getError().getMessage());
                return;
            }

            // OCR 결과 출력
            System.out.println("=== OCR TEXT ===");            
            System.out.println(response.getFullTextAnnotation().getText());
            System.out.println(response.getFullTextAnnotation().getAllFields());
            System.out.println(response.getFullTextAnnotation());
        }
    }
}