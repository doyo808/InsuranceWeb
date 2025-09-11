package com.kd.insuranceweb.mall.vo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.mall.vo.InsuranceProduct;


@Mapper
public interface InsuranceProductMapper {

    List<InsuranceProduct> findAll();

    InsuranceProduct findById(@Param("p_id") Long id);

    void insert(InsuranceProduct product);

    void update(InsuranceProduct product);

    void delete(@Param("p_id") Long id);
}
