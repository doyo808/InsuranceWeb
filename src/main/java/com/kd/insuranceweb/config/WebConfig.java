package com.kd.insuranceweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploaded/** 로 들어오는 요청을
        // 실제 로컬 경로 {프로젝트루트}/uploaded/ 로 연결시킴
    	String uploadedPath = "file:" + System.getProperty("user.dir").replace("\\", "/") + "/uploaded/";
        registry.addResourceHandler("/uploaded/**")
                .addResourceLocations(uploadedPath);
    }
}
