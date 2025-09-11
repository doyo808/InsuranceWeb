package com.kd.insuranceweb.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kd.insuranceweb.mall.vo.InsuranceProduct;
import com.kd.insuranceweb.mall.vo.mapper.InsuranceProductMapper;


@Controller
@RequestMapping("/admin/insurance")
public class AdminInsuranceController {

    @Autowired
    private InsuranceProductMapper productMapper;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productMapper.findAll());
        return "/admin/admin-insurance-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new InsuranceProduct());
        return "/admin/admin-insurance-form";
    }

    @GetMapping("/edit/{p_id}")
    public String edit(@PathVariable("p_id") Long id, Model model) {
        InsuranceProduct product = productMapper.findById(id);
        System.out.println(product);
        model.addAttribute("product", product);
        return "/admin/admin-insurance-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute InsuranceProduct product) {
        if (product.getP_id() == null) {
            productMapper.insert(product);
        } else {
            productMapper.update(product);
        }
        return "redirect:/admin/insurance";
    }

    @PostMapping("/delete/{p_id}")
    public String delete(@PathVariable("p_id") Long id) {
    	System.out.println("pId: "+id);
        productMapper.delete(id);
        return "redirect:/admin/insurance";
    }
}

