package com.kd.insuranceweb.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kd.insuranceweb.admin.dto.UiPathNoticeDto;

@Mapper
public interface UiPathMapper {
	UiPathNoticeDto SelectOne();
}
