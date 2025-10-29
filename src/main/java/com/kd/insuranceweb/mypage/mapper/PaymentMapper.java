package com.kd.insuranceweb.mypage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.mypage.dto.PaymentDto;

@Mapper
public interface PaymentMapper {
	List<PaymentDto> selectPayments(@Param("customer_id") Integer customer_id);
	int insertPayment(Map<String, Object> params);
    int updatePaymentToPaid(@Param("paymentId") int paymentId, @Param("paid_amount") int paid_amount);
}
