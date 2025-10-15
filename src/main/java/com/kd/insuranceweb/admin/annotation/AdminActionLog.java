package com.kd.insuranceweb.admin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminActionLog {
	String value(); // ex: "보험 계약 승인"
	String type() default""; // ex: "APPROVE_CONTRACT"
}
