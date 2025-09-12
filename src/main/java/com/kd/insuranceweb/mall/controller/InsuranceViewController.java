package com.kd.insuranceweb.mall.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.insuranceweb.mall.vo.InsuranceProduct;
import com.kd.insuranceweb.mall.vo.mapper.InsuranceProductMapper;

@Controller
@RequestMapping("/insurance")
public class InsuranceViewController {

    @Autowired
    private InsuranceProductMapper productMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/{pId}")
    public String viewInsurance(@PathVariable("pId") Long id, Model model) throws JsonProcessingException {
        InsuranceProduct product = productMapper.findById(id);
        System.out.println(product);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "보험 상품이 존재하지 않습니다");
        }

        System.out.println(product.getTabs_json());
        // JSON -> 객체 변환
		List<Map<String, Object>> tabs = objectMapper.readValue(product.getTabs_json(), new TypeReference<List<Map<String, Object>>>() {});
		List<Map<String, Object>> sections = objectMapper.readValue(product.getSections_json(), new TypeReference<List<Map<String, Object>>>() {});
		List<Map<String, Object>> buttons = objectMapper.readValue(product.getBottom_buttons_json(), new TypeReference<List<Map<String, Object>>>() {});

        model.addAttribute("product", product);
        model.addAttribute("tabs", tabs);
        model.addAttribute("sections", sections);
        model.addAttribute("buttons", buttons);
		
        return "/mall/layout";
    }
}
