package com.kd.insuranceweb.mall.vo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class InsuranceProduct {
    private Long p_id;
    private String title;
    private String badge;
    private String subtitle;
    private String descriptions;
    private String breadcrumb;
    private String main_button_text;
    private String top_image;
    private String tabs_json;
    private String sections_json;
    private String bottom_buttons_json;
}
