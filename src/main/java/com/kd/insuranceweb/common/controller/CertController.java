package com.kd.insuranceweb.common.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cert")
public class CertController {
	@PostMapping("/complete")
    public ResponseEntity<String> completeCert(@RequestBody Map<String, Object> certData) {
        return ResponseEntity.ok("본인인증 완료");
    }
}
